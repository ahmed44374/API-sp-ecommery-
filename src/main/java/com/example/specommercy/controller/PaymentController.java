//package com.example.specommercy.controller;
//
//import com.example.specommercy.payload.PaymentInfoRequest;
////import com.example.specommercy.service.PaymentService;
//import com.example.specommercy.util.AuthUtil;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/payment/secure")
//public class PaymentController {
//
//    private PaymentService paymentService;
//    private  AuthUtil authUtil;
//
//    @Autowired
//    public PaymentController(PaymentService paymentService, AuthUtil authUtil) {
//        this.paymentService = paymentService;
//        this.authUtil = authUtil;
//    }
//
//    @PostMapping("/payment-intent")
//    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest)
//            throws StripeException {
//
//        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
//        String paymentStr = paymentIntent.toJson();
//
//        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
//    }
//
//    @PutMapping("/payment-complete")
//    public ResponseEntity<String> stripePaymentComplete() throws Exception {
//        String userEmail = authUtil.loggedInUsername();
//        return paymentService.stripePayment(userEmail);
//    }
//}