package com.bijoy.events.billing_service.repository;

import com.bijoy.events.billing_service.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findBySso(String sso);
}
