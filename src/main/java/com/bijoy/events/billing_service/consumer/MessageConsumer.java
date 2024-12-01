package com.bijoy.events.billing_service.consumer;

import com.bijoy.events.billing_service.dto.PatientChargeDTO;
import com.bijoy.events.billing_service.dto.PatientCreationDTO;
import com.bijoy.events.billing_service.exception.PatientNotFoundException;
import com.bijoy.events.billing_service.model.Charge;
import com.bijoy.events.billing_service.model.Patient;
import com.bijoy.events.billing_service.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final PatientService patientService;

    public MessageConsumer(PatientService patientService) {
        this.patientService = patientService;
    }

    @RabbitListener(queues = {"${patient.charge.queue.name}"})
    public void consumerPatientCharge(PatientChargeDTO patientChargeDTO) {
        LOGGER.info("Charge details received for patient: {}", patientChargeDTO.getSso());
        try {
            Patient patient = patientService.findPatientBySso(patientChargeDTO.getSso());
            Charge charge = new Charge();
            charge.setCost(patientChargeDTO.getCost());
            charge.setTest(patientChargeDTO.getTest());
            patient.getCharges().add(charge);
            patient.setTotalCharge(patient.getTotalCharge() + charge.getCost());
            patientService.updatePatient(patient);
        } catch (PatientNotFoundException ex) {
            LOGGER.info("No patient found with sso: {}", patientChargeDTO.getSso());
        }
    }

    @RabbitListener(queues = {"${patient.creation.queue.name}"})
    public void consumerPatientDetails(Object patientDetails) {
        if (patientDetails instanceof PatientCreationDTO) {
            LOGGER.info("Patient creation request received: {}", ((PatientCreationDTO) patientDetails).getSso());
            patientService.savePatient((PatientCreationDTO) patientDetails);
        } else if (patientDetails instanceof PatientChargeDTO) {
            LOGGER.info("Patient charge details received for patient: {}", ((PatientChargeDTO) patientDetails).getSso());
            ;
        }
    }
}
