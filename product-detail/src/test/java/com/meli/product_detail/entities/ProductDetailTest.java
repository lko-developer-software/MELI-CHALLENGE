package com.meli.product_detail.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad ProductDetail.
 * 
 * Valida el comportamiento de la entidad principal,
 * relaciones JPA y validaciones de datos.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
class ProductDetailTest {

    private ProductDetail productDetail;

    @BeforeEach
    void setUp() {
        productDetail = new ProductDetail();
    }

    @Test
    void testProductDetailCreation_ShouldCreateWithDefaultValues() {
        // Given & When - ProductDetail is created in setUp()

        // Then
        assertNotNull(productDetail);
        assertNull(productDetail.getId());
        assertNull(productDetail.getProductId());
        assertNull(productDetail.getTitle());
        assertNull(productDetail.getCondition());
        assertNull(productDetail.getPrice());
        assertNull(productDetail.getAttributes());
        assertNull(productDetail.getShipping());
        assertNull(productDetail.getSellers());
    }

    @Test
    void testSettersAndGetters_ShouldWorkCorrectly() {
        // Given
        Long expectedId = 1L;
        String expectedProductId = "MLA123456789";
        String expectedTitle = "iPhone 13 Pro 128GB";
        String expectedCondition = "new";
        String expectedCategoryId = "MLA1055";
        String expectedListingTypeId = "gold_special";
        String expectedSiteId = "MLA";
        BigDecimal expectedPrice = new BigDecimal("599999.99");
        String expectedCurrencyId = "ARS";
        Integer expectedAvailableQuantity = 10;
        Integer expectedSoldQuantity = 5;
        String expectedBuyingMode = "buy_it_now";
        String expectedStatus = "active";
        String expectedPermalink = "https://articulo.mercadolibre.com.ar/MLA123456789";
        String expectedThumbnail = "https://http2.mlstatic.com/D_123456-MLA.jpg";
        List<String> expectedPictures = List.of("pic1.jpg", "pic2.jpg");

        // When
        productDetail.setId(expectedId);
        productDetail.setProductId(expectedProductId);
        productDetail.setTitle(expectedTitle);
        productDetail.setCondition(expectedCondition);
        productDetail.setCategoryId(expectedCategoryId);
        productDetail.setListingTypeId(expectedListingTypeId);
        productDetail.setSiteId(expectedSiteId);
        productDetail.setPrice(expectedPrice);
        productDetail.setCurrencyId(expectedCurrencyId);
        productDetail.setAvailableQuantity(expectedAvailableQuantity);
        productDetail.setSoldQuantity(expectedSoldQuantity);
        productDetail.setBuyingMode(expectedBuyingMode);
        productDetail.setStatus(expectedStatus);
        productDetail.setPermalink(expectedPermalink);
        productDetail.setThumbnail(expectedThumbnail);
        productDetail.setPictures(expectedPictures);

        // Then
        assertEquals(expectedId, productDetail.getId());
        assertEquals(expectedProductId, productDetail.getProductId());
        assertEquals(expectedTitle, productDetail.getTitle());
        assertEquals(expectedCondition, productDetail.getCondition());
        assertEquals(expectedCategoryId, productDetail.getCategoryId());
        assertEquals(expectedListingTypeId, productDetail.getListingTypeId());
        assertEquals(expectedSiteId, productDetail.getSiteId());
        assertEquals(expectedPrice, productDetail.getPrice());
        assertEquals(expectedCurrencyId, productDetail.getCurrencyId());
        assertEquals(expectedAvailableQuantity, productDetail.getAvailableQuantity());
        assertEquals(expectedSoldQuantity, productDetail.getSoldQuantity());
        assertEquals(expectedBuyingMode, productDetail.getBuyingMode());
        assertEquals(expectedStatus, productDetail.getStatus());
        assertEquals(expectedPermalink, productDetail.getPermalink());
        assertEquals(expectedThumbnail, productDetail.getThumbnail());
        assertEquals(expectedPictures, productDetail.getPictures());
    }

    @Test
    void testAttributesRelationship_ShouldManageCorrectly() {
        // Given
        List<Attribute> attributes = new ArrayList<>();
        
        Attribute colorAttribute = new Attribute();
        colorAttribute.setId(1L);
        colorAttribute.setName("Color");
        colorAttribute.setValueName("Azul");
        colorAttribute.setProductDetail(productDetail);
        
        Attribute brandAttribute = new Attribute();
        brandAttribute.setId(2L);
        brandAttribute.setName("Marca");
        brandAttribute.setValueName("Apple");
        brandAttribute.setProductDetail(productDetail);
        
        attributes.add(colorAttribute);
        attributes.add(brandAttribute);

        // When
        productDetail.setAttributes(attributes);

        // Then
        assertNotNull(productDetail.getAttributes());
        assertEquals(2, productDetail.getAttributes().size());
        assertEquals("Color", productDetail.getAttributes().get(0).getName());
        assertEquals("Azul", productDetail.getAttributes().get(0).getValueName());
        assertEquals("Marca", productDetail.getAttributes().get(1).getName());
        assertEquals("Apple", productDetail.getAttributes().get(1).getValueName());
        
        // Verify bidirectional relationship
        assertEquals(productDetail, colorAttribute.getProductDetail());
        assertEquals(productDetail, brandAttribute.getProductDetail());
    }

    @Test
    void testShippingRelationship_ShouldManageCorrectly() {
        // Given
        List<Shipping> shippingList = new ArrayList<>();
        
        Shipping shipping = new Shipping();
        shipping.setId(1L);
        shipping.setFreeShipping(true);
        shipping.setLogisticType("fulfillment");
        shipping.setMode("me2");
        shipping.setProductDetail(productDetail);
        
        shippingList.add(shipping);

        // When
        productDetail.setShipping(shippingList);

        // Then
        assertNotNull(productDetail.getShipping());
        assertEquals(1, productDetail.getShipping().size());
        assertTrue(productDetail.getShipping().get(0).isFreeShipping());
        assertEquals("fulfillment", productDetail.getShipping().get(0).getLogisticType());
        assertEquals("me2", productDetail.getShipping().get(0).getMode());
        
        // Verify bidirectional relationship
        assertEquals(productDetail, shipping.getProductDetail());
    }

    @Test
    void testSellersRelationship_ShouldManageCorrectly() {
        // Given
        List<Seller> sellers = new ArrayList<>();
        
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setNickname("VENDEDOR123");
        seller.setSellerType("professional");
        seller.setProductDetail(productDetail);
        
        sellers.add(seller);

        // When
        productDetail.setSellers(sellers);

        // Then
        assertNotNull(productDetail.getSellers());
        assertEquals(1, productDetail.getSellers().size());
        assertEquals("VENDEDOR123", productDetail.getSellers().get(0).getNickname());
        assertEquals("professional", productDetail.getSellers().get(0).getSellerType());
        
        // Verify bidirectional relationship
        assertEquals(productDetail, seller.getProductDetail());
    }

    @Test
    void testEqualsAndHashCode_ShouldWorkWithId() {
        // Given
        ProductDetail product1 = new ProductDetail();
        product1.setId(1L);
        product1.setProductId("MLA123456789");
        
        ProductDetail product2 = new ProductDetail();
        product2.setId(1L);
        product2.setProductId("MLA123456789");
        
        ProductDetail product3 = new ProductDetail();
        product3.setId(2L);
        product3.setProductId("MLA987654321");

        // Then - Test equals (Lombok generates this)
        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
        
        // Test hashCode (Lombok generates this)
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    void testToString_ShouldContainMainFields() {
        // Given
        productDetail.setId(1L);
        productDetail.setProductId("MLA123456789");
        productDetail.setTitle("iPhone 13 Pro");
        productDetail.setPrice(new BigDecimal("599999.99"));

        // When
        String toString = productDetail.toString();

        // Then - Lombok generates toString with all fields
        assertNotNull(toString);
        assertTrue(toString.contains("ProductDetail"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("productId=MLA123456789"));
        assertTrue(toString.contains("title=iPhone 13 Pro"));
        assertTrue(toString.contains("price=599999.99"));
    }

    @Test
    void testPriceHandling_ShouldHandleDecimalPrecision() {
        // Given
        BigDecimal highPrecisionPrice = new BigDecimal("599999.999999");
        BigDecimal zeroPrize = BigDecimal.ZERO;
        BigDecimal negativePrize = new BigDecimal("-100.00");

        // When & Then
        productDetail.setPrice(highPrecisionPrice);
        assertEquals(highPrecisionPrice, productDetail.getPrice());
        
        productDetail.setPrice(zeroPrize);
        assertEquals(zeroPrize, productDetail.getPrice());
        
        productDetail.setPrice(negativePrize);
        assertEquals(negativePrize, productDetail.getPrice());
    }

    @Test
    void testQuantityHandling_ShouldHandleEdgeCases() {
        // Given & When & Then
        productDetail.setAvailableQuantity(0);
        assertEquals(0, productDetail.getAvailableQuantity());
        
        productDetail.setAvailableQuantity(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, productDetail.getAvailableQuantity());
        
        productDetail.setSoldQuantity(null);
        assertNull(productDetail.getSoldQuantity());
    }

    @Test
    void testCollectionInitialization_ShouldHandleNullAndEmpty() {
        // Given & When
        productDetail.setAttributes(null);
        productDetail.setShipping(new ArrayList<>());
        productDetail.setSellers(null);

        // Then
        assertNull(productDetail.getAttributes());
        assertNotNull(productDetail.getShipping());
        assertTrue(productDetail.getShipping().isEmpty());
        assertNull(productDetail.getSellers());
    }

    @Test
    void testStringFieldsValidation_ShouldHandleEdgeCases() {
        // Given & When & Then
        productDetail.setProductId("");
        assertEquals("", productDetail.getProductId());
        
        productDetail.setTitle(null);
        assertNull(productDetail.getTitle());
        
        productDetail.setCondition("   ");
        assertEquals("   ", productDetail.getCondition());
        
        // Test very long strings
        String longString = "A".repeat(1000);
        productDetail.setTitle(longString);
        assertEquals(longString, productDetail.getTitle());
    }

    @Test
    void testPicturesList_ShouldHandleMultipleImages() {
        // Given
        List<String> pictures = List.of(
            "https://http2.mlstatic.com/image1.jpg",
            "https://http2.mlstatic.com/image2.jpg",
            "https://http2.mlstatic.com/image3.jpg",
            "https://http2.mlstatic.com/image4.jpg"
        );

        // When
        productDetail.setPictures(pictures);

        // Then
        assertNotNull(productDetail.getPictures());
        assertEquals(4, productDetail.getPictures().size());
        assertEquals("https://http2.mlstatic.com/image1.jpg", productDetail.getPictures().get(0));
        assertEquals("https://http2.mlstatic.com/image4.jpg", productDetail.getPictures().get(3));
    }
}