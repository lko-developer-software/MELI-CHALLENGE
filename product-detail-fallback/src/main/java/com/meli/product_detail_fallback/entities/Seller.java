package com.meli.product_detail_fallback.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;


/**
 * Información del vendedor de un producto.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@Entity
@Data
@Schema(description = "Entidad que representa un vendedor del producto")
public class Seller {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del vendedor", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "Nombre comercial o nickname del vendedor", example = "VENDEDOR123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;
    
    @Schema(description = "Tipo de vendedor en la plataforma", example = "professional", allowableValues = {"professional", "basic", "premium"})
    private String sellerType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product_detail")
    @JsonBackReference("productDetail-sellers")
    @Schema(description = "Referencia al producto que vende este vendedor")
    private ProductDetail productDetail;

    @OneToOne(mappedBy = "seller", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonManagedReference("seller-address")
    @Schema(description = "Dirección del vendedor")
    private SellerAddress address;

}
