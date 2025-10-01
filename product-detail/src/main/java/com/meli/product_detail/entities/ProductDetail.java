package com.meli.product_detail.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entidad principal que representa un producto de MercadoLibre.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@Entity
@Data
@Schema(description = "Detalle completo de un producto de MercadoLibre")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID interno del registro", example = "1")
    private Long id;
    
    @Schema(description = "ID único del producto en MercadoLibre", example = "MLA123456789", required = true)
    private String productId;
    
    @Schema(description = "Título del producto", example = "iPhone 13 Pro 128GB")
    private String title;
    
    @Schema(description = "Condición del producto", example = "new", allowableValues = {"new", "used", "not_specified"})
    private String condition;
    
    @Schema(description = "ID de categoría", example = "MLA1055")
    private String categoryId;
    
    @Schema(description = "Tipo de publicación", example = "gold_special")
    private String listingTypeId;
    
    @Schema(description = "Sitio de MercadoLibre", example = "MLA")
    private String siteId;
    
    @Schema(description = "Precio del producto", example = "599999.99")
    private BigDecimal price;
    
    @Schema(description = "Moneda del precio", example = "ARS")
    private String currencyId;
    
    @Schema(description = "Cantidad disponible", example = "10")
    private Integer availableQuantity;
    
    @Schema(description = "Cantidad vendida", example = "5")
    private Integer soldQuantity;
    
    @Schema(description = "Modo de compra", example = "buy_it_now")
    private String buyingMode;
    
    @Schema(description = "Estado de la publicación", example = "active")
    private String status;
    
    @Schema(description = "URL permanente del producto", example = "https://articulo.mercadolibre.com.ar/MLA123456789")
    private String permalink;
    
    @Schema(description = "URL de imagen miniatura", example = "https://http2.mlstatic.com/D_123456-MLA.jpg")
    private String thumbnail;
    
    @Schema(description = "Lista de URLs de imágenes del producto")
    private List<String> pictures;
    
    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonManagedReference("productDetail-attributes")
    @Schema(description = "Lista de atributos del producto (color, marca, etc.)")
    private List<Attribute> attributes;
    
    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonManagedReference("productDetail-shipping")
    @Schema(description = "Información de envío del producto")
    private List<Shipping> shipping;

    @OneToMany(mappedBy = "productDetail", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonManagedReference("productDetail-sellers")
    @Schema(description = "Información del/los vendedores")
    private List<Seller> sellers;

}
