package com.silvia.blogwebsite.services;

import com.silvia.blogwebsite.dto.AuthTokenDto;
import com.silvia.blogwebsite.models.Admin;

public interface LoginService {
    AuthTokenDto AdminLogin(Admin admin);
    AuthTokenDto getRefreshToken(String refreshToken);
}
