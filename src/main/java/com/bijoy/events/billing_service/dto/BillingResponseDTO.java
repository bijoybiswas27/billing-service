package com.bijoy.events.billing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingResponseDTO {
    private String name;
    private String sso;
    private double totalBill;
}
