package com.bijoy.events.billing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreationDTO {
    private String sso;
    private String firstName;
    private String lastName;
    private int age;
}
