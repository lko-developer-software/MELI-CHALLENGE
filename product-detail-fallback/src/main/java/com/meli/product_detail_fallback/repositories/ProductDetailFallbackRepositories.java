package com.meli.product_detail_fallback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meli.product_detail_fallback.entities.ProductDetail;

import java.util.Optional;

/**
 * Repositorio para la gestión de datos de ProductDetail.
 * 
 * Extiende JpaRepository para operaciones CRUD básicas
 * y define consultas personalizadas.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 * @version 1.0.0
 */
public interface ProductDetailFallbackRepositories extends JpaRepository<ProductDetail,Integer> {
    
    /**
     * Busca un producto por su ID único.
     * 
     * @param productId ID del producto
     * @return Optional con el producto encontrado
     */
    Optional<ProductDetail> findByProductId(String productId);
}
