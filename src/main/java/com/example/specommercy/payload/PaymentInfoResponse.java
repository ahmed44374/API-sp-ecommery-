package com.example.specommercy.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoResponse {
    private String status;
    private String responseMessage;
    private String sessionId;
    private String sessionUrl;
    private String pgName;
    private String pgMethod;
}
