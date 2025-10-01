package com.meli.product_detail_fallback.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;


/**
 * Dirección física de un vendedor.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@Entity
@Data
@Schema(description = "Entidad que representa la dirección de un vendedor")
public class SellerAddress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la dirección del vendedor", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seller")
    @JsonBackReference("seller-address")
    @Schema(description = "Referencia al vendedor propietario de esta dirección")
    private Seller seller;
    
    @Schema(description = "Ciudad donde se encuentra el vendedor", example = "Buenos Aires", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;
    
    @Schema(description = "Estado o provincia del vendedor", example = "Buenos Aires")
    private String state;
    
    @Schema(description = "País del vendedor", example = "Argentina", requiredMode = Schema.RequiredMode.REQUIRED)
    private String country;
    
    @Schema(description = "Código postal de la dirección", example = "C1425", pattern = "^[A-Z0-9]{4,10}$")
    private String zipCode;
}
