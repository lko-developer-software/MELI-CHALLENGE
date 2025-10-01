package com.meli.product_detail.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.core.annotation.Timed;

import com.meli.product_detail.annotations.GetAllProductDetails;
import com.meli.product_detail.annotations.GetProductDetailByProductId;
import com.meli.product_detail.annotations.ProductDetailController;
import com.meli.product_detail.annotations.ProductIdParameter;
import com.meli.product_detail.exceptions.MeliException;
import com.meli.product_detail.exceptions.SpanErrorHandler;
import com.meli.product_detail.services.ProductDetailService;

/**
 * Controlador REST para la gestión de detalles de productos.
 * 
 * Expone endpoints para consultar productos con trazabilidad distribuida,
 * métricas personalizadas y manejo centralizado de errores.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@RestController
@RequestMapping("detail")
@ProductDetailController
public class ProductDetailControllers {

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private Tracer tracer;

    /**
     * Obtiene todos los detalles de productos disponibles.
     * 
     * @return ResponseEntity con List&lt;ProductDetail&gt; incluyendo todos los productos
     *         con sus atributos, envío y vendedores. HTTP 200 si exitoso,
     *         HTTP 500 en errores internos.
     */
    @GetMapping
    @Timed(value = "product_detail.getAllProductDetails")  //metrica de actuator
    @GetAllProductDetails
    public ResponseEntity<?> getAllProductDetails() {
        // Crear span para trazabilidad distribuida
        Span span = tracer.nextSpan().name("ProductDetailControllers.getAllProductDetails").start();
        try {
            // Delegar al servicio y retornar respuesta exitosa
            return new ResponseEntity<>(productDetailService.getAllProductDetails(), HttpStatus.OK);
        } catch (MeliException ex) {
            // Etiquetar error en span y retornar respuesta con trace ID
            SpanErrorHandler.tagError(span, ex);
            return ResponseEntity.status(ex.getHttpStatus()).body(ex.toMeliExceptionDto(span));
        } finally {
            // Cerrar span para liberar recursos
            span.end();
        }
    }
    
    /**
     * Obtiene el detalle de un producto específico por su ID.
     * 
     * @param productId ID único del producto (formato MLA + números)
     * @return ResponseEntity con ProductDetail completo incluyendo atributos, 
     *         información de envío y datos del vendedor. HTTP 200 si existe,
     *         HTTP 404 si no se encuentra, HTTP 500 en errores internos.
     */
    @GetMapping("{productId}")
    @Timed(value = "product_detail.getProductDetailByProductId") //metrica de actuator
    @GetProductDetailByProductId
    public ResponseEntity<?> getProductDetailByProductId(@ProductIdParameter @PathVariable String productId) {
        // Crear span con nombre específico para trazabilidad
        Span span = tracer.nextSpan().name("ProductDetailControllers.getProductDetailByProductId").start();
        try {
            // Buscar producto por ID y retornar si existe
            return new ResponseEntity<>(productDetailService.getProductDetailByProductId(productId), HttpStatus.OK);
        } catch (MeliException ex) {
            // Capturar excepción, etiquetar span y generar respuesta con trace ID
            SpanErrorHandler.tagError(span, ex);
            return ResponseEntity.status(ex.getHttpStatus()).body(ex.toMeliExceptionDto(span));
        } finally {
            // Finalizar span independientemente del resultado
            span.end();
        }
    }
    
    
}
