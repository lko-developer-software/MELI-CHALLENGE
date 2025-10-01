package com.meli.auth_server.services;

import com.meli.auth_server.dtos.TokenDto;
import com.meli.auth_server.dtos.UserDto;
import com.meli.auth_server.exceptions.MeliException;

public interface AuthService {

    TokenDto login(UserDto user) throws MeliException;
    TokenDto validateToken(TokenDto token) throws MeliException;
    TokenDto refreshToken(String refreshToken) throws MeliException;
}
