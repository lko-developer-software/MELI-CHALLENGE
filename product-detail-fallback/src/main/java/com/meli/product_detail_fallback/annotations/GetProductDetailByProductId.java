package com.meli.product_detail_fallback.annotations;

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
 * Estereotipo personalizado para documentar el endpoint getProductDetailByProductId.
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
    summary = "Obtener detalle de producto por ID",
    description = "Retorna el detalle completo de un producto específico basado en su ID. "
                + "Incluye información detallada como atributos, datos de envío y información del vendedor."
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", 
        description = "Detalle del producto obtenido exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = com.meli.product_detail_fallback.entities.ProductDetail.class)
        )
    ),
    @ApiResponse(
        responseCode = "404", 
        description = "Producto no encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = com.meli.product_detail_fallback.exceptions.MeliExceptionDto.class)
        )
    ),
    @ApiResponse(
        responseCode = "500", 
        description = "Error interno del servidor",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = com.meli.product_detail_fallback.exceptions.MeliExceptionDto.class)
        )
    )
})
public @interface GetProductDetailByProductId {
}