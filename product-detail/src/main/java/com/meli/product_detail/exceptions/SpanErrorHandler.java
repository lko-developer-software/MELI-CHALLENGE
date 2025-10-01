package com.meli.product_detail.exceptions;

import io.micrometer.tracing.Span;

/**
 * Utilidad para manejar errores en spans de trazabilidad distribuida.
 * 
 * Proporciona métodos estáticos para etiquetar spans con información
 * de errores y generar trace IDs para correlación.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
public class SpanErrorHandler {

    /**
     * Etiqueta un span con información de error basada en la excepción proporcionada
     * 
     * @param span el span a etiquetar
     * @param ex la excepción que contiene la información del error
     */
    public static void tagError(Span span, Exception ex) {
        // Validar que span y excepción no sean nulos
        if (span != null && ex != null) {
            // Marcar span como error
            span.tag("error", "true");
            // Agregar mensaje de error o valor por defecto
            span.tag("error.message", ex.getMessage() != null ? ex.getMessage() : "Unknown error");
            // Agregar tipo simple de la excepción
            span.tag("error.type", ex.getClass().getSimpleName());
            // Agregar nombre completo de la clase de excepción
            span.tag("error.class", ex.getClass().getName());
            // Agregar trace ID para correlación
            span.tag("trace", span.context().traceId());
            // Preparar StringWriter para capturar stack trace
            java.io.StringWriter sw = new java.io.StringWriter();
            // Crear PrintWriter para formatear stack trace
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            ex.printStackTrace(pw);
            span.tag("error.stack", sw.toString());
            
            // Causa raíz si existe
            Throwable cause = ex.getCause();
            if (cause != null) {
                span.tag("error.cause.message", cause.getMessage() != null ? cause.getMessage() : "Unknown cause");
                span.tag("error.cause.type", cause.getClass().getSimpleName());
            }
            
            // Información específica de MeliException si es el caso
            if (ex instanceof com.meli.product_detail.exceptions.MeliException) {
                com.meli.product_detail.exceptions.MeliException meliEx = 
                    (com.meli.product_detail.exceptions.MeliException) ex;
                span.tag("error.http.status", String.valueOf(meliEx.getHttpStatus().value()));
                span.tag("error.http.status.name", meliEx.getHttpStatusName());
                if (meliEx.getErrorCode() != null) {
                    span.tag("error.meli.code", meliEx.getErrorCode());
                }
                if (meliEx.getRelatedObject() != null) {
                    span.tag("error.related.object", meliEx.getRelatedObject().toString());
                }
            }
        }
    }

    /**
     * Etiqueta un span con información de error y un mensaje personalizado
     * 
     * @param span el span a etiquetar
     * @param ex la excepción que contiene la información del error
     * @param customMessage mensaje personalizado para el error
     */
    public static void tagError(Span span, Exception ex, String customMessage) {
        if (span != null && ex != null) {
            span.tag("error", "true");
            span.tag("error.message", customMessage != null ? customMessage : 
                    (ex.getMessage() != null ? ex.getMessage() : "Unknown error"));
            span.tag("error.type", ex.getClass().getSimpleName());
            
        }
    }

    /**
     * Etiqueta un span con información de error usando solo un mensaje
     * 
     * @param span el span a etiquetar
     * @param errorMessage mensaje del error
     * @param errorType tipo de error
     */
    public static void tagError(Span span, String errorMessage, String errorType) {
        if (span != null) {
            span.tag("error", "true");
            span.tag("error.message", errorMessage != null ? errorMessage : "Unknown error");
            span.tag("error.type", errorType != null ? errorType : "UnknownError");
        }
    }
}