package com.example.specommercy.payload;

import lombok.Data;

@Data
public class PaymentInfoRequest {
    private int amount;
    private String currency;
    private String receiptEmail;
}