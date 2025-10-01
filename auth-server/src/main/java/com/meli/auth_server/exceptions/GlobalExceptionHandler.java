package com.meli.auth_server.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MeliException.class)
    public ResponseEntity<MeliExceptionDto> handleMeliException(MeliException ex) {
        log.error("MeliException occurred: {} - Status: {} - Code: {}", 
                  ex.getMessage(), ex.getHttpStatus(), ex.getErrorCode(), ex);
        
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.toMeliExceptionDto());
    }
}
