package com.meli.product_detail_fallback.services;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.meli.product_detail_fallback.entities.ProductDetail;
import com.meli.product_detail_fallback.exceptions.MeliException;
import com.meli.product_detail_fallback.exceptions.SpanErrorHandler;
import com.meli.product_detail_fallback.repositories.ProductDetailFallbackRepositories;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de detalles de productos.
 * 
 * Maneja la lógica de negocio con trazabilidad distribuida,
 * transacciones y logging estructurado.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Service
@Transactional
@Slf4j
public class ProductDetailFallbackServiceImpl implements ProductDetailFallbackService {

    @Autowired
    private ProductDetailFallbackRepositories repositories;

    @Autowired
    private Tracer tracer;

    @Override
    public List<ProductDetail> getAllProductDetails() throws MeliException {
        // Iniciar span para tracking de la operación
        Span span = tracer.nextSpan().name("ProductDetailFallbackServiceImpl.getAllProductDetails").start();
        try {          
            // Consultar todos los productos en la base de datos
            List<ProductDetail> result = repositories.findAll();
            // Retornar la lista obtenida
            return result;
        } catch (Exception ex) {
            // Etiquetar error y lanzar excepción personalizada
            SpanErrorHandler.tagError(span, ex);
            throw new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "001001");
        } finally {
            // Cerrar span para liberar recursos
            span.end();
        }
            
    }
    
    @Override
    public ProductDetail getProductDetailByProductId(String productId) throws MeliException {
        // Crear span con ID del producto para trazabilidad
        Span span = tracer.nextSpan().name("ProductDetailFallbackServiceImpl.getProductDetailByProductId").start();
        try {
            // Buscar producto por ID, lanzar excepción si no existe
            return repositories.findByProductId(productId)
                                 .orElseThrow(() -> new NoSuchElementException());
        } catch (NoSuchElementException ex) {
            // Manejar caso cuando el producto no existe
            SpanErrorHandler.tagError(span, ex);
            throw new MeliException(HttpStatus.NOT_FOUND, productId, "001002");
        } catch (Exception ex) {
            // Manejar cualquier otro error interno
            SpanErrorHandler.tagError(span, ex);
            throw new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "001003");
        } finally {
            // Finalizar span sin importar el resultado
            span.end();
        }
    }

}
