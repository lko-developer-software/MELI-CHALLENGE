package com.meli.auth_server.services;

import com.meli.auth_server.dtos.TokenDto;
import com.meli.auth_server.dtos.UserDto;
import com.meli.auth_server.entities.UserEntity;
import com.meli.auth_server.exceptions.MeliException;
import com.meli.auth_server.helpers.JwtHelper;
import com.meli.auth_server.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Implementación del servicio de autenticación.
 * 
 * Maneja la lógica de negocio de autenticación con JWT,
 * validación de usuarios y manejo de excepciones personalizadas.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Override
    public TokenDto login(UserDto user) throws MeliException {
        try {
            final var userFromDB = this.userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new NoSuchElementException());

            this.validPassword(user, userFromDB);
            return this.jwtHelper.createCompleteToken(userFromDB);
        } catch (NoSuchElementException ex) {
            // Usuario no encontrado
            log.error("User not found: {}", user.getUsername());
            throw new MeliException(HttpStatus.UNAUTHORIZED, user.getUsername(), "AUTH001");
        } catch (MeliException ex) {
            // Re-lanzar MeliException (puede venir de validPassword)
            throw ex;
        } catch (Exception ex) {
            // Error interno del servidor
            log.error("Internal error during login for user: {}", user.getUsername(), ex);
            throw new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "AUTH003");
        }
    }

    @Override
    public TokenDto validateToken(TokenDto token) throws MeliException {
        try {
            if (this.jwtHelper.validateToken(token.getAccessToken())) {
                return TokenDto.builder()
                        .accessToken(token.getAccessToken())
                        .tokenType("Bearer")
                        .resource("MELI Challenge")
                        .build();
            }
            // Token inválido
            log.error("Invalid JWT token provided");
            throw new MeliException(HttpStatus.UNAUTHORIZED, null, "AUTH004");
        } catch (Exception ex) {
            // Error al validar token
            log.error("Error validating JWT token", ex);
            throw new MeliException(HttpStatus.UNAUTHORIZED, null, "AUTH005");
        }
    }
    
    @Override
    public TokenDto refreshToken(String refreshToken) throws MeliException {
        try {
            // Validar que el refresh token sea válido
            if (!this.jwtHelper.validateToken(refreshToken)) {
                log.error("Invalid refresh token provided");
                throw new MeliException(HttpStatus.UNAUTHORIZED, null, "AUTH006");
            }
            
            // Validar que sea efectivamente un refresh token
            if (!this.jwtHelper.isRefreshToken(refreshToken)) {
                log.error("Provided token is not a refresh token");
                throw new MeliException(HttpStatus.UNAUTHORIZED, null, "AUTH007");
            }
            
            // Obtener el username del refresh token
            String username = this.jwtHelper.getUsernameFromToken(refreshToken);

            // Buscar el usuario completo
            UserEntity userFromDB = this.userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("User no longer exists for refresh token: {}", username);
                        return new MeliException(HttpStatus.UNAUTHORIZED, username, "AUTH008");
                    });

            // Generar nuevo token completo con todos los datos
            return this.jwtHelper.createCompleteToken(userFromDB);
            
        } catch (MeliException ex) {
            // Re-lanzar MeliException
            throw ex;
        } catch (Exception ex) {
            // Error interno al refrescar token
            log.error("Internal error during token refresh", ex);
            throw new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "AUTH009");
        }
    }

    private void validPassword(UserDto userDto, UserEntity userEntity) throws MeliException {
        if (!this.passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())) {
            log.error("Invalid password for user: {}", userDto.getUsername());
            throw new MeliException(HttpStatus.UNAUTHORIZED, userDto.getUsername(), "AUTH002");
        }
    }
}
