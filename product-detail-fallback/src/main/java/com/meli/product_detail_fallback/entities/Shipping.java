package com.meli.product_detail_fallback.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;


/**
 * Información de envío de un producto.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@Entity
@Data
@Schema(description = "Entidad que representa las opciones de envío de un producto")
public class Shipping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la opción de envío", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product_detail")
    @JsonBackReference("productDetail-shipping")
    @Schema(description = "Referencia al producto al que pertenece esta opción de envío")
    private ProductDetail productDetail;
    
    @Schema(description = "Indica si el envío es gratuito", example = "true")
    private boolean freeShipping;
    
    @Schema(description = "Tipo de logística utilizada para el envío", example = "fulfillment")
    private String logisticType;
    
    @Schema(description = "Modalidad de envío", example = "me2")
    private String mode;

}
