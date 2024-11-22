package com.example.specommercy.security.service;

import com.example.specommercy.security.request.LoginRequest;
import com.example.specommercy.security.request.SignupRequest;
import com.example.specommercy.security.response.MessageResponse;
import com.example.specommercy.security.response.UserInfoResponse;
import org.aspectj.bridge.Message;

public interface AuthService {
    UserInfoResponse login(LoginRequest loginRequest);

    MessageResponse signup(SignupRequest signupRequest);

}
