package com.example.specommercy.controller;
import com.example.specommercy.security.request.LoginRequest;
import com.example.specommercy.security.request.SignupRequest;
import com.example.specommercy.security.response.MessageResponse;
import com.example.specommercy.security.response.UserInfoResponse;
import com.example.specommercy.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserInfoResponse userInfoResponse = authService.login(loginRequest);
        return new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody SignupRequest signupRequest) {
        MessageResponse response = authService.signup(signupRequest);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }




}

