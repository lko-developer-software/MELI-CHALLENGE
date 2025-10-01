package com.meli.product_detail_fallback.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.micrometer.tracing.Span;

/**
 * Excepción personalizada para errores del dominio MELI.
 * 
 * Maneja errores con códigos HTTP, mensajes contextuales
 * y integración con trazabilidad distribuida.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
public class MeliException extends Exception {
   
    private final HttpStatus httpStatus;
    private final Object relatedObject;
    private final String errorCode;
    
    // Enum para manejar mensajes según HttpStatus
    public enum StatusMessage {
        NOT_FOUND(HttpStatus.NOT_FOUND, "Resource with ID %s not found"),
        BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request for resource %s"),
        UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized access to resource %s"),
        FORBIDDEN(HttpStatus.FORBIDDEN, "Access forbidden for resource %s"),
        INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error processing resource %s"),
        SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable for resource %s"),
        CONFLICT(HttpStatus.CONFLICT, "Conflict detected for resource %s"),
        UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unable to process resource %s");
        
        private final HttpStatus status;
        private final String messageTemplate;
        
        StatusMessage(HttpStatus status, String messageTemplate) {
            this.status = status;
            this.messageTemplate = messageTemplate;
        }
        
        public HttpStatus getStatus() {
            return status;
        }
        
        public String formatMessage(Object relatedObject, String errorCode) {
            String baseMessage;
            if (relatedObject == null) {
                baseMessage = messageTemplate.replace(" %s", "").replace("%s ", "");
            } else {
                baseMessage = String.format(messageTemplate, relatedObject);
            }
            
            // Concatenar el código de error al mensaje
            if (errorCode != null && !errorCode.trim().isEmpty()) {
                return "[" + errorCode + "] " + baseMessage;
            }
            return baseMessage;
        }
        
        public static StatusMessage fromHttpStatus(HttpStatus httpStatus) {
            for (StatusMessage sm : values()) {
                if (sm.status == httpStatus) {
                    return sm;
                }
            }
            return INTERNAL_SERVER_ERROR;
        }
    }
    
    // Constructor principal que usa el enum con código de error
    public MeliException(HttpStatus httpStatus, Object relatedObject, String errorCode) {
        super(StatusMessage.fromHttpStatus(httpStatus).formatMessage(relatedObject, errorCode));
        this.httpStatus = httpStatus;
        this.relatedObject = relatedObject;
        this.errorCode = errorCode;
    }
    
    // Constructor sin objeto relacionado pero con código
    public MeliException(HttpStatus httpStatus, String errorCode) {
        super(StatusMessage.fromHttpStatus(httpStatus).formatMessage(null, errorCode));
        this.httpStatus = httpStatus;
        this.relatedObject = null;
        this.errorCode = errorCode;
    }
    
    // Constructor con mensaje personalizado y código
    public MeliException(String message, HttpStatus httpStatus, Object relatedObject, String errorCode) {
        super(errorCode != null && !errorCode.trim().isEmpty() ? "[" + errorCode + "] " + message : message);
        this.httpStatus = httpStatus;
        this.relatedObject = relatedObject;
        this.errorCode = errorCode;
    }
    
    // Constructor con causa y código
    public MeliException(HttpStatus httpStatus, Object relatedObject, String errorCode, Throwable cause) {
        super(StatusMessage.fromHttpStatus(httpStatus).formatMessage(relatedObject, errorCode), cause);
        this.httpStatus = httpStatus;
        this.relatedObject = relatedObject;
        this.errorCode = errorCode;
    }
    
    // Constructores de compatibilidad (sin código de error)
    public MeliException(HttpStatus httpStatus, Object relatedObject) {
        this(httpStatus, relatedObject, null);
    }
    
    public MeliException(HttpStatus httpStatus) {
        this(httpStatus, null, null);
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    
    public Object getRelatedObject() {
        return relatedObject;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getHttpStatusName() {
        return httpStatus.getReasonPhrase();
    }
    
    public int getHttpStatusValue() {
        return httpStatus.value();
    }
    
    // Método para generar automáticamente el MeliExceptionDto
    public MeliExceptionDto toMeliExceptionDto(String requestPath) {
        return MeliExceptionDto.of(
            this.getHttpStatusName(),
            this.getMessage(),
            this.getHttpStatusValue(),
            requestPath
        );
    }

     // Nuevo método para recibir SpanContext y modificar solo el campo trace
    public MeliExceptionDto toMeliExceptionDto(Span span) {
        String traceId = (span != null && span.context() != null) ? span.context().traceId() : "";
        String requestPath = getCurrentRequestPath();
        return MeliExceptionDto.of(
            this.getHttpStatusName(),
            this.getMessage(),
            this.getHttpStatusValue(),
            requestPath,
            traceId
        );
    }
    
    
    // Método sin parámetros que obtiene automáticamente el path de la request
    public MeliExceptionDto toMeliExceptionDto() {
        String requestPath = getCurrentRequestPath();
        return MeliExceptionDto.of(
            this.getHttpStatusName(),
            this.getMessage(),
            this.getHttpStatusValue(),
            requestPath
        );
    }

    
    
    // Sobrecarga con trace personalizado
    public MeliExceptionDto toMeliExceptionDto(String requestPath, String trace) {
        return MeliExceptionDto.of(
            this.getHttpStatusName(),
            this.getMessage(),
            this.getHttpStatusValue(),
            requestPath,
            trace
        );
    }
    
    
    // Método privado para obtener el path de la request actual
    private String getCurrentRequestPath() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest().getRequestURI();
        } catch (Exception e) {
            // Fallback si no hay contexto de request disponible
            return "/unknown";
        }
    }
}