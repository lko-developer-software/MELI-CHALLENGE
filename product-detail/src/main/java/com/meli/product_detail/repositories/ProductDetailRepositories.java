package com.meli.product_detail.repositories;

import com.meli.product_detail.entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

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
public interface ProductDetailRepositories extends JpaRepository<ProductDetail,Integer> {
    
    /**
     * Busca un producto por su ID único.
     * 
     * @param productId ID del producto
     * @return Optional con el producto encontrado
     */
    Optional<ProductDetail> findByProductId(String productId);
}
