package com.meli.product_detail.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.meli.product_detail.entities.ProductDetail;
import com.meli.product_detail.exceptions.MeliException;

/**
 * Servicio para la gestión de detalles de productos.
 * 
 * Define las operaciones de negocio para consultar productos
 * con manejo de excepciones personalizadas.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
@Service
public interface ProductDetailService {
    
    /**
     * Obtiene todos los detalles de productos.
     * 
     * @return Lista de productos
     * @throws MeliException si ocurre un error interno
     */
    List<ProductDetail> getAllProductDetails() throws MeliException;
    
    /**
     * Obtiene un producto por su ID.
     * 
     * @param productId ID del producto
     * @return Detalle del producto
     * @throws MeliException si el producto no existe o hay error interno
     */
    ProductDetail getProductDetailByProductId(String productId) throws MeliException;
}
