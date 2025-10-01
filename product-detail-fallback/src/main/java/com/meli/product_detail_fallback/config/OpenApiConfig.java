package com.meli.product_detail_fallback.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API.
 * 
 * Define la información básica y metadatos de la API REST
 * para generar documentación interactiva.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Respaldo de Detalle de Producto - Desafío MELI",
        version = "1.0.0",
        description = "Microservicio encargado de administrar detalles de productos"
    )
)
public class OpenApiConfig {

}
