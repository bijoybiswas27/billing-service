package com.bijoy.events.billing_service.controller;

import com.bijoy.events.billing_service.dto.BillingResponseDTO;
import com.bijoy.events.billing_service.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/billings")
public class BillingController {
    private final PatientService patientService;

    public BillingController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/{sso}")
    public ResponseEntity<BillingResponseDTO> getBillingDetails(@PathVariable String sso) {
        return ResponseEntity.ok(patientService.getBilling(sso));
    }
}
