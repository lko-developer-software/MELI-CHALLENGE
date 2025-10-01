package com.meli.auth_server.exceptions;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeliExceptionDto {
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;
    private int status;
    
    private String error;
    
    private String trace;
    
    private String message;
    
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