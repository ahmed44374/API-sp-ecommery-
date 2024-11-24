package com.example.specommercy.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfoRequest {
    private double amount;
    private String currency;
    private Long quantity;
    private String name;
}