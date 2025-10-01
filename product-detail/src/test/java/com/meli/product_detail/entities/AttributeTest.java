package com.meli.product_detail.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Attribute.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
class AttributeTest {

    private Attribute attribute;
    private ProductDetail productDetail;

    @BeforeEach
    void setUp() {
        attribute = new Attribute();
        productDetail = new ProductDetail();
        productDetail.setId(1L);
        productDetail.setProductId("MLA123456789");
    }

    @Test
    void testAttributeCreation_ShouldCreateWithDefaultValues() {
        // Then
        assertNotNull(attribute);
        assertNull(attribute.getId());
        assertNull(attribute.getName());
        assertNull(attribute.getValueName());
        assertNull(attribute.getProductDetail());
    }

    @Test
    void testSettersAndGetters_ShouldWorkCorrectly() {
        // Given
        Long expectedId = 1L;
        String expectedName = "Color";
        String expectedValueName = "Azul";

        // When
        attribute.setId(expectedId);
        attribute.setName(expectedName);
        attribute.setValueName(expectedValueName);
        attribute.setProductDetail(productDetail);

        // Then
        assertEquals(expectedId, attribute.getId());
        assertEquals(expectedName, attribute.getName());
        assertEquals(expectedValueName, attribute.getValueName());
        assertEquals(productDetail, attribute.getProductDetail());
    }

    @Test
    void testProductDetailRelationship_ShouldMaintainBidirectionalReference() {
        // When
        attribute.setProductDetail(productDetail);

        // Then
        assertEquals(productDetail, attribute.getProductDetail());
        assertEquals("MLA123456789", attribute.getProductDetail().getProductId());
    }

    @Test
    void testEqualsAndHashCode_WithLombok() {
        // Given
        Attribute attribute1 = new Attribute();
        attribute1.setId(1L);
        attribute1.setName("Color");
        attribute1.setValueName("Azul");

        Attribute attribute2 = new Attribute();
        attribute2.setId(1L);
        attribute2.setName("Color");
        attribute2.setValueName("Azul");

        Attribute attribute3 = new Attribute();
        attribute3.setId(2L);
        attribute3.setName("Marca");
        attribute3.setValueName("Apple");

        // Then
        assertEquals(attribute1, attribute2);
        assertNotEquals(attribute1, attribute3);
        assertEquals(attribute1.hashCode(), attribute2.hashCode());
        assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    void testToString_ShouldContainFields() {
        // Given
        attribute.setId(1L);
        attribute.setName("Color");
        attribute.setValueName("Azul");

        // When
        String toString = attribute.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Attribute"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Color"));
        assertTrue(toString.contains("valueName=Azul"));
    }

    @Test
    void testNullValues_ShouldHandleGracefully() {
        // When
        attribute.setName(null);
        attribute.setValueName(null);
        attribute.setProductDetail(null);

        // Then
        assertNull(attribute.getName());
        assertNull(attribute.getValueName());
        assertNull(attribute.getProductDetail());
    }

    @Test
    void testEmptyStrings_ShouldHandleCorrectly() {
        // When
        attribute.setName("");
        attribute.setValueName("");

        // Then
        assertEquals("", attribute.getName());
        assertEquals("", attribute.getValueName());
    }

    @Test
    void testSpecialCharacters_ShouldHandleCorrectly() {
        // Given
        String nameWithSpecialChars = "Tamaño de pantalla";
        String valueWithSpecialChars = "6.1\"";

        // When
        attribute.setName(nameWithSpecialChars);
        attribute.setValueName(valueWithSpecialChars);

        // Then
        assertEquals(nameWithSpecialChars, attribute.getName());
        assertEquals(valueWithSpecialChars, attribute.getValueName());
    }
}