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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para SpanErrorHandler.
 * 
 * Valida el etiquetado de spans con información de errores
 * para trazabilidad distribuida.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SpanErrorHandlerTest {

    @Mock
    private Span span;

    @Mock
    private TraceContext traceContext;

    @BeforeEach
    void setUp() {
        when(span.context()).thenReturn(traceContext);
        when(traceContext.traceId()).thenReturn("test-trace-id-123456789");
    }

    @Test
    void testTagError_WithException_ShouldTagAllFields() {
        // Given
        RuntimeException exception = new RuntimeException("Test error message");

        // When
        SpanErrorHandler.tagError(span, exception);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq("Test error message"));
        verify(span).tag(eq("error.type"), eq("RuntimeException"));
        verify(span).tag(eq("error.class"), eq("java.lang.RuntimeException"));
        verify(span).tag(eq("trace"), eq("test-trace-id-123456789"));
        verify(span).tag(eq("error.stack"), anyString());
        
        // Verify context access
        verify(span).context();
        verify(traceContext).traceId();
    }

    @Test
    void testTagError_WithMeliException_ShouldTagSpecificFields() {
        // Given
        MeliException meliException = new MeliException(HttpStatus.NOT_FOUND, "MLA123456789", "001002");

        // When
        SpanErrorHandler.tagError(span, meliException);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), anyString());
        verify(span).tag(eq("error.type"), eq("MeliException"));
        verify(span).tag(eq("error.class"), eq("com.meli.product_detail.exceptions.MeliException"));
        verify(span).tag(eq("trace"), eq("test-trace-id-123456789"));
        verify(span).tag(eq("error.stack"), anyString());
        
        // Verify MeliException specific tags
        verify(span).tag(eq("error.http.status"), eq("404"));
        verify(span).tag(eq("error.http.status.name"), eq("Not Found"));
        verify(span).tag(eq("error.meli.code"), eq("001002"));
        verify(span).tag(eq("error.related.object"), eq("MLA123456789"));
    }

    @Test
    void testTagError_WithExceptionWithCause_ShouldTagCause() {
        // Given
        IllegalArgumentException cause = new IllegalArgumentException("Invalid argument provided");
        RuntimeException exception = new RuntimeException("Wrapper exception", cause);

        // When
        SpanErrorHandler.tagError(span, exception);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq("Wrapper exception"));
        verify(span).tag(eq("error.type"), eq("RuntimeException"));
        
        // Verify cause tags
        verify(span).tag(eq("error.cause.message"), eq("Invalid argument provided"));
        verify(span).tag(eq("error.cause.type"), eq("IllegalArgumentException"));
    }

    @Test
    void testTagError_WithNullException_ShouldHandleGracefully() {
        // When
        SpanErrorHandler.tagError(span, null);

        // Then
        verifyNoInteractions(span);
    }

    @Test
    void testTagError_WithNullSpan_ShouldHandleGracefully() {
        // Given
        RuntimeException exception = new RuntimeException("Test error");

        // When
        SpanErrorHandler.tagError(null, exception);

        // Then
        verifyNoInteractions(span);
    }

    @Test
    void testTagError_WithBothNull_ShouldHandleGracefully() {
        // When
        SpanErrorHandler.tagError(null, null);

        // Then
        verifyNoInteractions(span);
    }

    @Test
    void testTagError_WithExceptionNullMessage_ShouldUseDefault() {
        // Given
        RuntimeException exception = new RuntimeException((String) null);

        // When
        SpanErrorHandler.tagError(span, exception);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq("Unknown error"));
        verify(span).tag(eq("error.type"), eq("RuntimeException"));
        verify(span).tag(eq("error.class"), eq("java.lang.RuntimeException"));
    }

    @Test
    void testTagError_WithCauseNullMessage_ShouldUseDefault() {
        // Given
        IllegalArgumentException cause = new IllegalArgumentException((String) null);
        RuntimeException exception = new RuntimeException("Main exception", cause);

        // When
        SpanErrorHandler.tagError(span, exception);

        // Then
        verify(span).tag(eq("error.cause.message"), eq("Unknown cause"));
        verify(span).tag(eq("error.cause.type"), eq("IllegalArgumentException"));
    }

    @Test
    void testTagError_WithMeliExceptionNullFields_ShouldHandleGracefully() {
        // Given
        MeliException meliException = new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, null);

        // When
        SpanErrorHandler.tagError(span, meliException);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.http.status"), eq("500"));
        verify(span).tag(eq("error.http.status.name"), eq("Internal Server Error"));
        
        // Should not tag null fields
        verify(span, never()).tag(eq("error.meli.code"), anyString());
        verify(span, never()).tag(eq("error.related.object"), anyString());
    }

    @Test
    void testTagError_WithCustomMessage_ShouldUseCustomMessage() {
        // Given
        RuntimeException exception = new RuntimeException("Original message");
        String customMessage = "Custom error message for testing";

        // When
        SpanErrorHandler.tagError(span, exception, customMessage);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq(customMessage));
        verify(span).tag(eq("error.type"), eq("RuntimeException"));
    }

    @Test
    void testTagError_WithCustomMessageNull_ShouldFallbackToException() {
        // Given
        RuntimeException exception = new RuntimeException("Exception message");

        // When
        SpanErrorHandler.tagError(span, exception, null);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq("Exception message"));
        verify(span).tag(eq("error.type"), eq("RuntimeException"));
    }

    @Test
    void testTagError_WithCustomMessageAndNullException_ShouldUseCustom() {
        // Given
        String customMessage = "Custom error without exception";

        // When
        SpanErrorHandler.tagError(span, (Exception) null, customMessage);

        // Then
        verifyNoInteractions(span);
    }

    @Test
    void testTagError_WithStringParameters_ShouldTagCorrectly() {
        // Given
        String errorMessage = "String-based error message";
        String errorType = "ValidationError";

        // When
        SpanErrorHandler.tagError(span, errorMessage, errorType);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq(errorMessage));
        verify(span).tag(eq("error.type"), eq(errorType));
    }

    @Test
    void testTagError_WithStringParametersNull_ShouldUseDefaults() {
        // When
        SpanErrorHandler.tagError(span, (String) null, (String) null);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq("Unknown error"));
        verify(span).tag(eq("error.type"), eq("UnknownError"));
    }

    @Test
    void testTagError_WithNullSpanStringParameters_ShouldHandleGracefully() {
        // When
        SpanErrorHandler.tagError(null, "Error message", "ErrorType");

        // Then
        verifyNoInteractions(span);
    }

    @Test
    void testTagError_StackTraceHandling_ShouldIncludeStackTrace() {
        // Given
        RuntimeException exception = new RuntimeException("Test exception for stack trace");

        // When
        SpanErrorHandler.tagError(span, exception);

        // Then
        verify(span).tag(eq("error.stack"), anyString());
        
        // Verify that the stack trace contains expected information
        // We can't easily verify the exact content, but we can verify the tag was called
        verify(span, times(1)).tag(eq("error.stack"), anyString());
    }

    @Test
    void testTagError_WithComplexExceptionHierarchy_ShouldHandleCorrectly() {
        // Given
        IllegalStateException rootCause = new IllegalStateException("Root cause message");
        IllegalArgumentException midCause = new IllegalArgumentException("Mid level cause", rootCause);
        RuntimeException topException = new RuntimeException("Top level exception", midCause);

        // When
        SpanErrorHandler.tagError(span, topException);

        // Then
        verify(span).tag(eq("error"), eq("true"));
        verify(span).tag(eq("error.message"), eq("Top level exception"));
        verify(span).tag(eq("error.type"), eq("RuntimeException"));
        
        // Should tag the direct cause (midCause)
        verify(span).tag(eq("error.cause.message"), eq("Mid level cause"));
        verify(span).tag(eq("error.cause.type"), eq("IllegalArgumentException"));
        
        // Stack trace should contain all levels
        verify(span).tag(eq("error.stack"), anyString());
    }
}