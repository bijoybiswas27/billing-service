package com.bijoy.events.billing_service.service;

import com.bijoy.events.billing_service.dto.BillingResponseDTO;
import com.bijoy.events.billing_service.dto.PatientCreationDTO;
import com.bijoy.events.billing_service.exception.PatientNotFoundException;
import com.bijoy.events.billing_service.model.Patient;
import com.bijoy.events.billing_service.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PatientService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    private PatientRepository patientRepository;

    public Patient findPatientBySso(String sso) {
        return patientRepository.findBySso(sso)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with the sso: " + sso));
    }

    public void updatePatient(Patient patient) {
        patientRepository.save(patient);
        LOGGER.info("Patients details getting updated into DB: {}", patient.getSso());
    }

    public void savePatient(PatientCreationDTO patientCreationDTO) {
        Optional<Patient> optionalPatient = patientRepository.findBySso(patientCreationDTO.getSso());
        if (optionalPatient.isPresent()) {
            LOGGER.info("Patient already present in DB: {}", patientCreationDTO.getSso());
            return;
        }
        Patient patient = new Patient();
        patient.setSso(patientCreationDTO.getSso());
        patient.setTotalCharge(0.0);
        patient.setAge(patientCreationDTO.getAge());
        patient.setCharges(new ArrayList<>());
        patient.setName(patientCreationDTO.getFirstName() + " " + patientCreationDTO.getLastName());
        patientRepository.save(patient);
        LOGGER.info("New patient created in the DB: {}", patient.getSso());
    }

    public BillingResponseDTO getBilling(String sso) {
        Patient patient = findPatientBySso(sso);
        LOGGER.info("Populating the billing response details for patient: {}", sso);
        BillingResponseDTO billingResponseDTO = new BillingResponseDTO();
        billingResponseDTO.setSso(sso);
        billingResponseDTO.setName(patient.getName());
        billingResponseDTO.setTotalBill(patient.getTotalCharge());
        return billingResponseDTO;
    }
}
