package com.example.specommercy.service;

import com.example.specommercy.model.Payment;
import com.example.specommercy.payload.PaymentInfoRequest;
import com.example.specommercy.payload.PaymentInfoResponse;
import com.example.specommercy.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StripePaymentService implements PaymentService{

    @Value("${stripe.apiKey}")
    private String secretKey;
    @Override
    public PaymentInfoResponse checkoutProducts(List<PaymentInfoRequest> paymentInfoRequests) throws StripeException {
        Stripe.apiKey = secretKey;
        List<SessionCreateParams.LineItem> lineItems = paymentInfoRequests.stream()
                .map(item ->
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(item.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(item.getCurrency())
                                                .setUnitAmount((long) item.getAmount() * 100)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(item.getName())
                                                                .build()
                                                ).build()

                                )
                                .build()
                ).toList();
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://localhost:8081/api/payment/success")
                .setCancelUrl("https://localhost:8081/api/payment/cancel")
                .addAllLineItem(lineItems)
                .build();
        Session session = Session.create(params);
        PaymentInfoResponse response = new PaymentInfoResponse();
        response.setStatus("SUCCESS");
        response.setResponseMessage("Checkout session created successfully.");
        response.setSessionId(session.getId());
        response.setSessionUrl(session.getUrl());
        response.setPgMethod("stripe");
        response.setPgMethod("CARD");

        return response;
    }
}
