package com.example.specommercy.service;

import com.example.specommercy.payload.OrderDTO;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;

@Transactional
public interface OrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) throws StripeException;
}
