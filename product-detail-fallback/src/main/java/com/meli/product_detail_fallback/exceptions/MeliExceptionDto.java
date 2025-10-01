package com.meli.product_detail_fallback.exceptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta estándar de error del sistema")
public class MeliExceptionDto {
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Schema(description = "Fecha y hora del error", example = "2025-09-29T08:14:14.851-05:00")
    private ZonedDateTime timestamp;
    @Schema(description = "Código de estado HTTP", example = "500/404")
    private int status;
    
    @Schema(description = "Nombre del error HTTP", example = "Internal Server Error/Not Found")
    private String error;
    
    @Schema(description = "ID de traza para seguimiento", example = "68da8b018a68a1fb84a1c9713b3d2ed6")
    private String trace;
    
    @Schema(description = "Mensaje descriptivo del error", example = "[001002] Resource with ID XXXXXXXX not found")
    private String message;
    
    @Schema(description = "Ruta del endpoint que generó el error", example = "/product-detail/detail/xxxxxxxx")
    private String path;
    
    public static MeliExceptionDto of(String error, String message, int status, String path) {
        return MeliExceptionDto.builder()
                .timestamp(ZonedDateTime.now())
                .status(status)
                .error(error)
                .trace("")
                .message(message)
                .path(path)
                .build();
    }
    
    public static MeliExceptionDto of(String error, String message, int status, String path, String trace) {
        return MeliExceptionDto.builder()
                .timestamp(ZonedDateTime.now())
                .status(status)
                .error(error)
                .trace(trace != null ? trace : "")
                .message(message)
                .path(path)
                .build();
    }
}