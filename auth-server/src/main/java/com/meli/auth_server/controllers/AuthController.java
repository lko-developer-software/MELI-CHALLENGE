package com.meli.auth_server.controllers;

import com.meli.auth_server.dtos.TokenDto;
import com.meli.auth_server.dtos.UserDto;
import com.meli.auth_server.exceptions.MeliException;
import com.meli.auth_server.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de autenticación.
 * 
 * Expone endpoints para login y validación de tokens JWT
 * con manejo centralizado de errores usando MeliException.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@RestController
@RequestMapping(path = "auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * @param user Credenciales del usuario (username y password)
     * @return ResponseEntity con TokenDto conteniendo el JWT generado. 
     *         HTTP 200 si exitoso, HTTP 401 si credenciales inválidas,
     *         HTTP 500 en errores internos.
     */
    @PostMapping(path = "login")
    public ResponseEntity<?> jwtCreate(@RequestBody UserDto user) {
        try {
            TokenDto token = this.authService.login(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (MeliException ex) {
            log.error("Authentication failed for user: {} - Error: {}", user.getUsername(), ex.getMessage());
            return ResponseEntity.status(ex.getHttpStatus()).body(ex.toMeliExceptionDto());
        }
    }

    /**
     * Valida un token JWT existente.
     * 
     * @param accessToken Token JWT a validar recibido en el header
     * @return ResponseEntity con TokenDto si el token es válido.
     *         HTTP 200 si el token es válido, HTTP 401 si es inválido,
     *         HTTP 500 en errores internos.
     */
    @PostMapping(path = "jwt")
    public ResponseEntity<?> jwtValidate(@RequestHeader String accessToken) {
        try {
            TokenDto tokenDto = TokenDto.builder().accessToken(accessToken).build();
            TokenDto validatedToken = this.authService.validateToken(tokenDto);
            return new ResponseEntity<>(validatedToken, HttpStatus.OK);
        } catch (MeliException ex) {
            log.error("Token validation failed - Error: {}", ex.getMessage());
            return ResponseEntity.status(ex.getHttpStatus()).body(ex.toMeliExceptionDto());
        }
    }

    /**
     * Refresca un token JWT usando un refresh token.
     * 
     * @param refreshToken Refresh token para generar un nuevo access token
     * @return ResponseEntity con TokenDto completo incluyendo nuevo access token y refresh token.
     *         HTTP 200 si exitoso, HTTP 401 si el refresh token es inválido,
     *         HTTP 500 en errores internos.
     */
    @PostMapping(path = "refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            TokenDto newTokens = this.authService.refreshToken(refreshToken);
            return new ResponseEntity<>(newTokens, HttpStatus.OK);
        } catch (MeliException ex) {
            log.error("Token refresh failed - Error: {}", ex.getMessage());
            return ResponseEntity.status(ex.getHttpStatus()).body(ex.toMeliExceptionDto());
        }
    }
}
