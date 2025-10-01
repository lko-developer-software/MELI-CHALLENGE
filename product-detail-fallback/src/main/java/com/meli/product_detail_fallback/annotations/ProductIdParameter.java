package com.meli.product_detail_fallback.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Estereotipo personalizado para documentar el parámetro productId.
 * 
 * Define validaciones y documentación del parámetro productId
 * de manera centralizada y reutilizable.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
    description = "ID único del producto a consultar", 
    required = true,
    example = "MLA123456789",
    schema = @Schema(
        type = "string",
        pattern = "^MLA\\d+$",
        minLength = 10,
        maxLength = 15
    )
)
public @interface ProductIdParameter {
}