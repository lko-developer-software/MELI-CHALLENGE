package com.meli.auth_server.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de tokens JWT.
 * 
 * Contiene información completa del token de acceso incluyendo
 * refresh token, tiempo de expiración y metadatos.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("refresh_token")
    private String refreshToken;
    
    @JsonProperty("expires_in")
    private Long expiresIn;
    
    @JsonProperty("token_type")
    @Builder.Default
    private String tokenType = "Bearer";
    
    @JsonProperty("resource")
    @Builder.Default
    private String resource = "MELI Challenge";
}
