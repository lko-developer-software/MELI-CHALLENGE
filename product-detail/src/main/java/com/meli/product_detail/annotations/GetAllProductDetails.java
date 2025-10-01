package com.meli.product_detail.annotations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Estereotipo personalizado para documentar el endpoint getAllProductDetails.
 * 
 * Centraliza la documentación Swagger para mantener el código limpio
 * y reutilizable en múltiples controladores.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = "Obtener todos los detalles de productos",
    description = "Retorna la lista completa de todos los detalles de productos disponibles en el sistema. "
                + "Este endpoint proporciona información detallada de productos incluyendo atributos, envío y vendedor."
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de productos obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = com.meli.product_detail.entities.ProductDetail.class)
        )
    ),
    @ApiResponse(
        responseCode = "500", 
        description = "Error interno del servidor",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = com.meli.product_detail.exceptions.MeliExceptionDto.class)
        )
    )
})
public @interface GetAllProductDetails {
}