package com.example.specommercy.security.service;

import com.example.specommercy.security.request.LoginRequest;
import com.example.specommercy.security.response.UserInfoResponse;

public interface AuthService {
    public UserInfoResponse login(LoginRequest loginRequest);
}
