package com.meli.product_detail.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple para depurar el problema de MeliException
 */
class SimpleMeliExceptionTest {

    @Test
    void testSimpleConstructor() {
        // Test más simple posible
        MeliException exception = new MeliException(HttpStatus.NOT_FOUND, "TEST123", "001002");
        
        System.out.println("HttpStatus: " + exception.getHttpStatus());
        System.out.println("RelatedObject: " + exception.getRelatedObject());
        System.out.println("ErrorCode: " + exception.getErrorCode());
        
        assertNotNull(exception.getHttpStatus());
        assertNotNull(exception.getRelatedObject());
        assertNotNull(exception.getErrorCode());
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("TEST123", exception.getRelatedObject());
        assertEquals("001002", exception.getErrorCode());
    }
    
    @Test
    void testCompatibilityConstructor() {
        // Test del constructor de compatibilidad
        MeliException exception = new MeliException(HttpStatus.NOT_FOUND, "TEST123");
        
        System.out.println("HttpStatus: " + exception.getHttpStatus());
        System.out.println("RelatedObject: " + exception.getRelatedObject());
        System.out.println("ErrorCode: " + exception.getErrorCode());
        
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("TEST123", exception.getRelatedObject());
        assertNull(exception.getErrorCode());
    }
}