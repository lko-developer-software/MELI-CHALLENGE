package com.meli.product_detail.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Atributo específico de un producto (color, marca, etc.).
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@Entity
@Data
@Schema(description = "Entidad que representa un atributo específico de un producto")
public class Attribute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del atributo", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product_detail")
    @JsonBackReference("productDetail-attributes")
    @Schema(description = "Referencia al producto al que pertenece este atributo")
    private ProductDetail productDetail;
    
    @Schema(description = "Nombre del atributo del producto", example = "Color", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    
    @Schema(description = "Valor específico del atributo", example = "Azul", requiredMode = Schema.RequiredMode.REQUIRED)
    private String valueName;
    
}
