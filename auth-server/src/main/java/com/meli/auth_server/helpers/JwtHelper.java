package com.meli.auth_server.helpers;

import com.meli.auth_server.dtos.TokenDto;
import com.meli.auth_server.exceptions.MeliException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Helper para manejo de tokens JWT.
 * 
 * Proporciona funcionalidades para crear y validar tokens JWT
 * con manejo de excepciones personalizado.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Component
@Slf4j
public class JwtHelper {

    @Value("${application.jwt.secret}")
    private String jwtSecret;

    @Value("${application.expirationTime}")
    private Integer jwtExpirationInMs;
    
    // Refresh token expira en 7 días (7 * 24 * 60 * 60 * 1000)
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000L;

    /**
     * Crea un TokenDto completo con access token y refresh token.
     */
    public TokenDto createCompleteToken(com.meli.auth_server.entities.UserEntity user) {
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user.getUsername());
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn((long) jwtExpirationInMs / 1000)
                .tokenType("Bearer")
                .resource("MELI Challenge")
                .build();
    }

    /**
     * Crea solo el access token (mantener compatibilidad).
     */
    public String createToken(String username) {
        return createAccessToken(com.meli.auth_server.entities.UserEntity.builder().username(username).build());
    }

    /**
     * Crea un access token JWT con todos los datos del usuario como claims.
     */
    private String createAccessToken(com.meli.auth_server.entities.UserEntity user) {
        final var now = new Date();
        final var expirationDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts
                .builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .claim("type", "access_token")
                    .claim("id", user.getId())
                    .claim("nombre", user.getNombre())
                    .claim("apellido", user.getApellido())
                    .claim("email", user.getEmail())
                    .claim("telefono", user.getTelefono())
                    .claim("direccion", user.getDireccion())
                    .claim("rol", user.getRol())
                    .claim("cargo", user.getCargo())
                    .claim("ml_id", user.getMlId())
                    .claim("estado", user.getEstado())
                    .claim("fecha_registro", user.getFechaRegistro())
                    .signWith(this.getSecretKey())
                .compact();
    }
    
    /**
     * Crea un refresh token JWT.
     */
    private String createRefreshToken(String username) {
        final var now = new Date();
        final var expirationDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);
        return Jwts
                .builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .claim("type", "refresh_token")
                    .signWith(this.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token) throws MeliException {
        try {
            final var expirationDate = this.getExpirationDate(token);
            return expirationDate.after(new Date());
        } catch (Exception e) {
            log.error("JWT token validation failed: {}", e.getMessage());
            throw new MeliException(HttpStatus.UNAUTHORIZED, null, "JWT001");
        }
    }
    
    /**
     * Obtiene el username del token.
     */
    public String getUsernameFromToken(String token) throws MeliException {
        try {
            return this.getClaimsFromToken(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            throw new MeliException(HttpStatus.UNAUTHORIZED, null, "JWT002");
        }
    }
    
    /**
     * Valida si es un refresh token.
     */
    public boolean isRefreshToken(String token) throws MeliException {
        try {
            Claims claims = this.signToken(token);
            return "refresh_token".equals(claims.get("type"));
        } catch (Exception e) {
            log.error("Failed to validate refresh token: {}", e.getMessage());
            throw new MeliException(HttpStatus.UNAUTHORIZED, null, "JWT003");
        }
    }
    private Date getExpirationDate(String token) {
        return this.getClaimsFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> resolver) {
        return resolver.apply(this.signToken(token));
    }

    private Claims signToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(this.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
