package com.meli.product_detail.annotations;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Estereotipo personalizado para documentar el controlador de Product Detail.
 * 
 * Define la etiqueta principal del controlador REST en la documentación
 * OpenAPI de manera centralizada y reutilizable.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag(name = "Detalle del Producto", description = "APIs de gestión de detalles de productos - DESAFÍO MELI")
public @interface ProductDetailController {
}