package com.meli.product_detail_fallback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Aplicación principal del microservicio Product Detail.
 * 
 * Microservicio para gestionar detalles de productos de MercadoLibre con
 * trazabilidad distribuida, métricas Prometheus y documentación OpenAPI.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProductDetailFallbackApplication {

	/**
	 * Punto de entrada principal de la aplicación.
	 * 
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProductDetailFallbackApplication.class, args);
		System.out.println("\n\n\n=================================");
		System.out.println("         MELI Challenge            ");
		System.out.println("=================================");
		System.out.println("--- Product detail fallback -----");
		System.out.println("=================================");
		System.out.println("Ejecutando: http://localhost:8181");
		System.out.println("=================================");
	}

}
