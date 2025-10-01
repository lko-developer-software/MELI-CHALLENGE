package com.meli.product_detail.exceptions;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

/**
 * Tests unitarios para MeliException.
 * 
 * Valida el comportamiento de excepciones personalizadas,
 * manejo de códigos HTTP y generación de DTOs.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MeliExceptionTest {

    @Mock
    private Span span;

    @Mock
    private TraceContext traceContext;

    @BeforeEach
    void setUp() {
        lenient().when(span.context()).thenReturn(traceContext);
        lenient().when(traceContext.traceId()).thenReturn("test-trace-id-123456789");
    }

    @Test
    void testMeliExceptionCreation_WithHttpStatusAndErrorCode() {
        // Given
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        String expectedProductId = "MLA123456789";
        String expectedErrorCode = "001002";

        // When
        MeliException exception = new MeliException(expectedStatus, expectedProductId, expectedErrorCode);

        // Then
        assertEquals(expectedStatus, exception.getHttpStatus());
        assertEquals(expectedProductId, exception.getRelatedObject());
        assertEquals(expectedErrorCode, exception.getErrorCode());
        assertEquals("Not Found", exception.getHttpStatusName());
        assertEquals(404, exception.getHttpStatusValue());
        assertTrue(exception.getMessage().contains(expectedErrorCode));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testMeliExceptionCreation_WithoutRelatedObject() {
        // Given
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String expectedErrorCode = "001001";

        // When
        MeliException exception = new MeliException(expectedStatus, null, expectedErrorCode);

        // Then
        assertEquals(expectedStatus, exception.getHttpStatus());
        assertNull(exception.getRelatedObject()); // relatedObject es null ya que se pasó null
        assertEquals(expectedErrorCode, exception.getErrorCode());
        assertEquals(500, exception.getHttpStatusValue());
        assertTrue(exception.getMessage().contains(expectedErrorCode));
    }

    @Test
    void testMeliExceptionCreation_WithCustomMessage() {
        // Given
        String customMessage = "Custom error message for testing";
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        String expectedProductId = "MLA123456789";
        String expectedErrorCode = "001004";

        // When
        MeliException exception = new MeliException(customMessage, expectedStatus, expectedProductId, expectedErrorCode);

        // Then
        assertEquals(expectedStatus, exception.getHttpStatus());
        assertEquals(expectedProductId, exception.getRelatedObject());
        assertEquals(expectedErrorCode, exception.getErrorCode());
        assertTrue(exception.getMessage().contains(customMessage));
        assertTrue(exception.getMessage().contains(expectedErrorCode));
    }

    @Test
    void testMeliExceptionCreation_WithCause() {
        // Given
        RuntimeException cause = new RuntimeException("Database connection failed");
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String expectedProductId = "MLA123456789";
        String expectedErrorCode = "001003";

        // When
        MeliException exception = new MeliException(expectedStatus, expectedProductId, expectedErrorCode, cause);

        // Then
        assertEquals(expectedStatus, exception.getHttpStatus());
        assertEquals(expectedProductId, exception.getRelatedObject());
        assertEquals(expectedErrorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getMessage().contains(expectedErrorCode));
    }

    @Test
    void testBackwardCompatibilityConstructors() {
        // Test constructor with all parameters first (should work)
        MeliException exceptionWithCode = new MeliException(HttpStatus.NOT_FOUND, "MLA123456789", "001002");
        assertEquals(HttpStatus.NOT_FOUND, exceptionWithCode.getHttpStatus());
        assertEquals("MLA123456789", exceptionWithCode.getRelatedObject());
        assertEquals("001002", exceptionWithCode.getErrorCode());
        
        // Test constructor without error code  
        MeliException exceptionWithoutCode = new MeliException(HttpStatus.NOT_FOUND, "MLA123456789");
        assertEquals(HttpStatus.NOT_FOUND, exceptionWithoutCode.getHttpStatus());
        assertEquals("MLA123456789", exceptionWithoutCode.getRelatedObject());
        assertNull(exceptionWithoutCode.getErrorCode());

        // Test constructor with only status
        MeliException exceptionStatusOnly = new MeliException(HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exceptionStatusOnly.getHttpStatus());
        assertNull(exceptionStatusOnly.getRelatedObject());
        assertNull(exceptionStatusOnly.getErrorCode());
    }

    @Test
    void testStatusMessageEnum_AllHttpStatuses() {
        // Test NOT_FOUND
        MeliException.StatusMessage notFound = MeliException.StatusMessage.fromHttpStatus(HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatus());
        String notFoundMessage = notFound.formatMessage("MLA123", "001002");
        assertTrue(notFoundMessage.contains("001002"));
        assertTrue(notFoundMessage.contains("MLA123"));
        assertTrue(notFoundMessage.contains("not found"));

        // Test INTERNAL_SERVER_ERROR
        MeliException.StatusMessage internalError = MeliException.StatusMessage.fromHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, internalError.getStatus());
        String internalErrorMessage = internalError.formatMessage(null, "001001");
        assertTrue(internalErrorMessage.contains("001001"));
        assertTrue(internalErrorMessage.contains("Internal server error"));

        // Test BAD_REQUEST
        MeliException.StatusMessage badRequest = MeliException.StatusMessage.fromHttpStatus(HttpStatus.BAD_REQUEST);
        assertEquals(HttpStatus.BAD_REQUEST, badRequest.getStatus());
        String badRequestMessage = badRequest.formatMessage("invalidParam", "001005");
        assertTrue(badRequestMessage.contains("001005"));
        assertTrue(badRequestMessage.contains("Invalid request"));
    }

    @Test
    void testStatusMessageEnum_UnknownStatus() {
        // Given an unknown but valid status
        HttpStatus customStatus = HttpStatus.I_AM_A_TEAPOT; // Status code 418

        // When
        MeliException.StatusMessage result = MeliException.StatusMessage.fromHttpStatus(customStatus);

        // Then - Should default to INTERNAL_SERVER_ERROR
        assertEquals(MeliException.StatusMessage.INTERNAL_SERVER_ERROR, result);
    }

    @Test
    void testToMeliExceptionDto_WithSpan() {
        // Given
        MeliException exception = new MeliException(HttpStatus.NOT_FOUND, "MLA123456789", "001002");

        // When
        MeliExceptionDto dto = exception.toMeliExceptionDto(span);

        // Then
        assertNotNull(dto);
        assertEquals(404, dto.getStatus());
        assertEquals("Not Found", dto.getError());
        assertEquals("test-trace-id-123456789", dto.getTrace());
        assertTrue(dto.getMessage().contains("001002"));
        assertTrue(dto.getMessage().contains("not found"));
        assertNotNull(dto.getTimestamp());
        assertNotNull(dto.getPath());
    }

    @Test
    void testToMeliExceptionDto_WithoutSpan() {
        // Given
        MeliException exception = new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "001001");

        // When
        MeliExceptionDto dto = exception.toMeliExceptionDto((Span) null);

        // Then
        assertNotNull(dto);
        assertEquals(500, dto.getStatus());
        assertEquals("Internal Server Error", dto.getError());
        assertEquals("", dto.getTrace()); // Empty trace when span is null
        assertTrue(dto.getMessage().contains("001001"));
    }

    @Test
    void testToMeliExceptionDto_WithCustomPath() {
        // Given
        MeliException exception = new MeliException(HttpStatus.NOT_FOUND, "MLA123456789", "001002");
        String customPath = "/product-detail/detail/MLA123456789";

        // When
        MeliExceptionDto dto = exception.toMeliExceptionDto(customPath);

        // Then
        assertNotNull(dto);
        assertEquals(404, dto.getStatus());
        assertEquals("Not Found", dto.getError());
        assertEquals(customPath, dto.getPath());
        assertTrue(dto.getMessage().contains("001002"));
    }

    @Test
    void testToMeliExceptionDto_WithCustomPathAndTrace() {
        // Given
        MeliException exception = new MeliException(HttpStatus.BAD_REQUEST, "invalidParam", "001005");
        String customPath = "/product-detail/detail";
        String customTrace = "custom-trace-abc123";

        // When
        MeliExceptionDto dto = exception.toMeliExceptionDto(customPath, customTrace);

        // Then
        assertNotNull(dto);
        assertEquals(400, dto.getStatus());
        assertEquals("Bad Request", dto.getError());
        assertEquals(customPath, dto.getPath());
        assertEquals(customTrace, dto.getTrace());
        assertTrue(dto.getMessage().contains("001005"));
        assertTrue(dto.getMessage().contains("Invalid request"));
    }

    @Test
    void testErrorCodeHandling_NullAndEmpty() {
        // Test with null error code
        MeliException exceptionNullCode = new MeliException(HttpStatus.NOT_FOUND, "MLA123", null);
        assertNull(exceptionNullCode.getErrorCode());
        assertFalse(exceptionNullCode.getMessage().contains("["));

        // Test with empty error code
        MeliException exceptionEmptyCode = new MeliException(HttpStatus.NOT_FOUND, "MLA123", "");
        assertEquals("", exceptionEmptyCode.getErrorCode());
        assertFalse(exceptionEmptyCode.getMessage().contains("["));

        // Test with whitespace error code
        MeliException exceptionWhitespaceCode = new MeliException(HttpStatus.NOT_FOUND, "MLA123", "   ");
        assertEquals("   ", exceptionWhitespaceCode.getErrorCode());
        assertFalse(exceptionWhitespaceCode.getMessage().contains("["));
    }

    @Test
    void testMessageFormattingWithNullRelatedObject() {
        // Given
        MeliException exception = new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "001001");

        // When
        String message = exception.getMessage();

        // Then
        assertTrue(message.contains("001001"));
        assertTrue(message.contains("Internal server error"));
        // Message should not contain "%s" or placeholder text when relatedObject is null
        assertFalse(message.contains("%s"));
        assertFalse(message.contains("resource null"));
    }

    @Test
    void testAllHttpStatusesSupported() {
        // Test all supported HTTP statuses
        HttpStatus[] supportedStatuses = {
            HttpStatus.NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            HttpStatus.UNAUTHORIZED,
            HttpStatus.FORBIDDEN,
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.SERVICE_UNAVAILABLE,
            HttpStatus.CONFLICT,
            HttpStatus.UNPROCESSABLE_ENTITY
        };

        for (HttpStatus status : supportedStatuses) {
            MeliException exception = new MeliException(status, "testObject", "TEST001");
            assertEquals(status, exception.getHttpStatus());
            assertEquals(status.value(), exception.getHttpStatusValue());
            assertEquals(status.getReasonPhrase(), exception.getHttpStatusName());
            assertNotNull(exception.getMessage());
            assertTrue(exception.getMessage().contains("TEST001"));
        }
    }
}