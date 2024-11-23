//package com.example.specommercy.service;
//
//import com.example.specommercy.model.Payment;
//import com.example.specommercy.payload.PaymentInfoRequest;
//import com.example.specommercy.repository.PaymentRepository;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@Transactional
//public class StripePaymentService implements PaymentService{
//    private PaymentRepository paymentRepository;
//
//    @Autowired
//    public StripePaymentService(PaymentRepository paymentRepository, @Value("${stripe.apiKey}") String secretKey) {
//        this.paymentRepository = paymentRepository;
//        Stripe.apiKey = secretKey;
//    }
//
//    @Override
//    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
//        List<String> paymentMethodTypes = new ArrayList<>();
//        paymentMethodTypes.add("card");
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("amount", paymentInfoRequest.getAmount());
//        params.put("currency", paymentInfoRequest.getCurrency());
//        params.put("payment_method_types", paymentMethodTypes);
//
//        return PaymentIntent.create(params);
//    }
//
//    @Override
//    public ResponseEntity<String> stripePayment(String userEmail) throws Exception {
//        Payment payment = paymentRepository.findByUserEmail(userEmail);
//
//        if (payment == null) {
//            throw new Exception("Payment information is missing");
//        }
//        paymentRepository.save(payment);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//}
