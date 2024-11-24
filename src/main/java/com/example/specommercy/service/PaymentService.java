package com.example.specommercy.service;

import com.example.specommercy.payload.PaymentInfoRequest;
import com.example.specommercy.payload.PaymentInfoResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.StripeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    PaymentInfoResponse checkoutProducts(List<PaymentInfoRequest> paymentInfoRequests) throws StripeException;

//    ResponseEntity<String> stripePayment(String userEmail) throws Exception;
}
