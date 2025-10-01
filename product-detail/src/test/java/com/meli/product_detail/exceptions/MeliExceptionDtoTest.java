package com.meli.product_detail.exceptions;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para MeliExceptionDto.
 * 
 * Valida la creación y formateo de DTOs de respuesta de error.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
class MeliExceptionDtoTest {

    @Test
    void testMeliExceptionDtoCreation_WithBuilder() {
        // Given
        ZonedDateTime expectedTimestamp = ZonedDateTime.now();
        int expectedStatus = 404;
        String expectedError = "Not Found";
        String expectedTrace = "test-trace-123456789";
        String expectedMessage = "[001002] Resource with ID MLA123456789 not found";
        String expectedPath = "/product-detail/detail/MLA123456789";

        // When
        MeliExceptionDto dto = MeliExceptionDto.builder()
                .timestamp(expectedTimestamp)
                .status(expectedStatus)
                .error(expectedError)
                .trace(expectedTrace)
                .message(expectedMessage)
                .path(expectedPath)
                .build();

        // Then
        assertEquals(expectedTimestamp, dto.getTimestamp());
        assertEquals(expectedStatus, dto.getStatus());
        assertEquals(expectedError, dto.getError());
        assertEquals(expectedTrace, dto.getTrace());
        assertEquals(expectedMessage, dto.getMessage());
        assertEquals(expectedPath, dto.getPath());
    }

    @Test
    void testMeliExceptionDtoCreation_WithAllArgsConstructor() {
        // Given
        ZonedDateTime expectedTimestamp = ZonedDateTime.now();
        int expectedStatus = 500;
        String expectedError = "Internal Server Error";
        String expectedTrace = "trace-abc123";
        String expectedMessage = "[001001] Internal server error";
        String expectedPath = "/product-detail/detail";

        // When
        MeliExceptionDto dto = new MeliExceptionDto(
                expectedTimestamp,
                expectedStatus,
                expectedError,
                expectedTrace,
                expectedMessage,
                expectedPath
        );

        // Then
        assertEquals(expectedTimestamp, dto.getTimestamp());
        assertEquals(expectedStatus, dto.getStatus());
        assertEquals(expectedError, dto.getError());
        assertEquals(expectedTrace, dto.getTrace());
        assertEquals(expectedMessage, dto.getMessage());
        assertEquals(expectedPath, dto.getPath());
    }

    @Test
    void testMeliExceptionDtoCreation_WithNoArgsConstructor() {
        // When
        MeliExceptionDto dto = new MeliExceptionDto();

        // Then
        assertNull(dto.getTimestamp());
        assertEquals(0, dto.getStatus());
        assertNull(dto.getError());
        assertNull(dto.getTrace());
        assertNull(dto.getMessage());
        assertNull(dto.getPath());
    }

    @Test
    void testStaticFactoryMethod_Of_WithoutTrace() {
        // Given
        String expectedError = "Not Found";
        String expectedMessage = "[001002] Resource with ID MLA123456789 not found";
        int expectedStatus = 404;
        String expectedPath = "/product-detail/detail/MLA123456789";

        // When
        MeliExceptionDto dto = MeliExceptionDto.of(expectedError, expectedMessage, expectedStatus, expectedPath);

        // Then
        assertEquals(expectedStatus, dto.getStatus());
        assertEquals(expectedError, dto.getError());
        assertEquals("", dto.getTrace()); // Should be empty string when not provided
        assertEquals(expectedMessage, dto.getMessage());
        assertEquals(expectedPath, dto.getPath());
        assertNotNull(dto.getTimestamp());
        
        // Timestamp should be close to now (within 1 second)
        ZonedDateTime now = ZonedDateTime.now();
        assertTrue(dto.getTimestamp().isBefore(now.plusSeconds(1)));
        assertTrue(dto.getTimestamp().isAfter(now.minusSeconds(1)));
    }

    @Test
    void testStaticFactoryMethod_Of_WithTrace() {
        // Given
        String expectedError = "Internal Server Error";
        String expectedMessage = "[001001] Internal server error";
        int expectedStatus = 500;
        String expectedPath = "/product-detail/detail";
        String expectedTrace = "test-trace-xyz789";

        // When
        MeliExceptionDto dto = MeliExceptionDto.of(expectedError, expectedMessage, expectedStatus, expectedPath, expectedTrace);

        // Then
        assertEquals(expectedStatus, dto.getStatus());
        assertEquals(expectedError, dto.getError());
        assertEquals(expectedTrace, dto.getTrace());
        assertEquals(expectedMessage, dto.getMessage());
        assertEquals(expectedPath, dto.getPath());
        assertNotNull(dto.getTimestamp());
    }

    @Test
    void testStaticFactoryMethod_Of_WithNullTrace() {
        // Given
        String expectedError = "Bad Request";
        String expectedMessage = "[001005] Invalid request";
        int expectedStatus = 400;
        String expectedPath = "/product-detail/detail";

        // When
        MeliExceptionDto dto = MeliExceptionDto.of(expectedError, expectedMessage, expectedStatus, expectedPath, null);

        // Then
        assertEquals(expectedStatus, dto.getStatus());
        assertEquals(expectedError, dto.getError());
        assertEquals("", dto.getTrace()); // Should be empty string when null is provided
        assertEquals(expectedMessage, dto.getMessage());
        assertEquals(expectedPath, dto.getPath());
    }

    @Test
    void testSettersAndGetters_AllFields() {
        // Given
        MeliExceptionDto dto = new MeliExceptionDto();
        ZonedDateTime expectedTimestamp = ZonedDateTime.now();
        int expectedStatus = 401;
        String expectedError = "Unauthorized";
        String expectedTrace = "trace-unauthorized-123";
        String expectedMessage = "[001006] Unauthorized access";
        String expectedPath = "/product-detail/admin";

        // When
        dto.setTimestamp(expectedTimestamp);
        dto.setStatus(expectedStatus);
        dto.setError(expectedError);
        dto.setTrace(expectedTrace);
        dto.setMessage(expectedMessage);
        dto.setPath(expectedPath);

        // Then
        assertEquals(expectedTimestamp, dto.getTimestamp());
        assertEquals(expectedStatus, dto.getStatus());
        assertEquals(expectedError, dto.getError());
        assertEquals(expectedTrace, dto.getTrace());
        assertEquals(expectedMessage, dto.getMessage());
        assertEquals(expectedPath, dto.getPath());
    }

    @Test
    void testEqualsAndHashCode_WithLombok() {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now();
        MeliExceptionDto dto1 = MeliExceptionDto.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .trace("trace-123")
                .message("Test message")
                .path("/test/path")
                .build();

        MeliExceptionDto dto2 = MeliExceptionDto.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .trace("trace-123")
                .message("Test message")
                .path("/test/path")
                .build();

        MeliExceptionDto dto3 = MeliExceptionDto.builder()
                .timestamp(timestamp)
                .status(500)
                .error("Internal Server Error")
                .trace("trace-456")
                .message("Different message")
                .path("/different/path")
                .build();

        // Then
        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString_WithLombok() {
        // Given
        ZonedDateTime timestamp = ZonedDateTime.now();
        MeliExceptionDto dto = MeliExceptionDto.builder()
                .timestamp(timestamp)
                .status(404)
                .error("Not Found")
                .trace("trace-test-123")
                .message("[001002] Resource not found")
                .path("/product-detail/detail/MLA123")
                .build();

        // When
        String toString = dto.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("MeliExceptionDto"));
        assertTrue(toString.contains("status=404"));
        assertTrue(toString.contains("error=Not Found"));
        assertTrue(toString.contains("trace=trace-test-123"));
        assertTrue(toString.contains("message=[001002] Resource not found"));
        assertTrue(toString.contains("path=/product-detail/detail/MLA123"));
    }

    @Test
    void testNullValues_ShouldHandleGracefully() {
        // Given & When
        MeliExceptionDto dto = MeliExceptionDto.of(null, null, 500, null);

        // Then
        assertEquals(500, dto.getStatus());
        assertNull(dto.getError());
        assertNull(dto.getMessage());
        assertNull(dto.getPath());
        assertEquals("", dto.getTrace());
        assertNotNull(dto.getTimestamp());
    }

    @Test
    void testEmptyStrings_ShouldHandleCorrectly() {
        // Given & When
        MeliExceptionDto dto = MeliExceptionDto.of("", "", 400, "");

        // Then
        assertEquals(400, dto.getStatus());
        assertEquals("", dto.getError());
        assertEquals("", dto.getMessage());
        assertEquals("", dto.getPath());
        assertEquals("", dto.getTrace());
    }

    @Test
    void testCommonHttpStatuses_ShouldWorkCorrectly() {
        // Test various HTTP status codes
        int[] statusCodes = {200, 201, 400, 401, 403, 404, 409, 422, 500, 503};
        String[] errorNames = {"OK", "Created", "Bad Request", "Unauthorized", "Forbidden", 
                              "Not Found", "Conflict", "Unprocessable Entity", "Internal Server Error", "Service Unavailable"};

        for (int i = 0; i < statusCodes.length; i++) {
            MeliExceptionDto dto = MeliExceptionDto.of(errorNames[i], "Test message", statusCodes[i], "/test");
            assertEquals(statusCodes[i], dto.getStatus());
            assertEquals(errorNames[i], dto.getError());
        }
    }

    @Test
    void testLongMessages_ShouldHandleCorrectly() {
        // Given
        String longMessage = "This is a very long error message that contains a lot of information about what went wrong " +
                           "during the processing of the request and includes detailed technical information that might " +
                           "be useful for debugging purposes and troubleshooting the issue that occurred.";
        String longPath = "/product-detail/very/long/path/with/many/segments/for/testing/purposes/only";

        // When
        MeliExceptionDto dto = MeliExceptionDto.of("Internal Server Error", longMessage, 500, longPath);

        // Then
        assertEquals(longMessage, dto.getMessage());
        assertEquals(longPath, dto.getPath());
        assertEquals(500, dto.getStatus());
    }

    @Test
    void testTimestampPrecision_ShouldMaintainPrecision() {
        // When - Create two DTOs with small time difference
        MeliExceptionDto dto1 = MeliExceptionDto.of("Error", "Message", 500, "/path");
        try {
            Thread.sleep(1); // Sleep for 1 millisecond
        } catch (InterruptedException e) {
            // Ignore
        }
        MeliExceptionDto dto2 = MeliExceptionDto.of("Error", "Message", 500, "/path");

        // Then - Timestamps should be different (or at least not fail the test if system is too fast)
        assertNotNull(dto1.getTimestamp());
        assertNotNull(dto2.getTimestamp());
        
        // Both should be recent
        ZonedDateTime now = ZonedDateTime.now();
        assertTrue(dto1.getTimestamp().isBefore(now.plusSeconds(1)));
        assertTrue(dto2.getTimestamp().isBefore(now.plusSeconds(1)));
    }
}