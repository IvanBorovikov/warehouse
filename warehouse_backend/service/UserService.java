package com.warehouse.warehouse_backend.service;

import com.warehouse.warehouse_backend.dto.token.JwtAuthenticationDto;
import com.warehouse.warehouse_backend.dto.token.RefreshTokenDto;
import com.warehouse.warehouse_backend.dto.token.UserCredentialsDto;
import com.warehouse.warehouse_backend.model.User;

import javax.naming.AuthenticationException;

public interface UserService {
    User registerUser(User user);
    JwtAuthenticationDto signIn(UserCredentialsDto dto) throws AuthenticationException;
    JwtAuthenticationDto refreshToken(RefreshTokenDto dto) throws Exception;

}
