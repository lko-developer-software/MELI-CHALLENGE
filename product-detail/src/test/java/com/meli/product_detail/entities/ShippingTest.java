package com.meli.product_detail.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Shipping.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
class ShippingTest {

    private Shipping shipping;
    private ProductDetail productDetail;

    @BeforeEach
    void setUp() {
        shipping = new Shipping();
        productDetail = new ProductDetail();
        productDetail.setId(1L);
        productDetail.setProductId("MLA123456789");
    }

    @Test
    void testShippingCreation_ShouldCreateWithDefaultValues() {
        // Then
        assertNotNull(shipping);
        assertNull(shipping.getId());
        assertFalse(shipping.isFreeShipping()); // boolean default is false
        assertNull(shipping.getLogisticType());
        assertNull(shipping.getMode());
        assertNull(shipping.getProductDetail());
    }

    @Test
    void testSettersAndGetters_ShouldWorkCorrectly() {
        // Given
        Long expectedId = 1L;
        boolean expectedFreeShipping = true;
        String expectedLogisticType = "fulfillment";
        String expectedMode = "me2";

        // When
        shipping.setId(expectedId);
        shipping.setFreeShipping(expectedFreeShipping);
        shipping.setLogisticType(expectedLogisticType);
        shipping.setMode(expectedMode);
        shipping.setProductDetail(productDetail);

        // Then
        assertEquals(expectedId, shipping.getId());
        assertEquals(expectedFreeShipping, shipping.isFreeShipping());
        assertEquals(expectedLogisticType, shipping.getLogisticType());
        assertEquals(expectedMode, shipping.getMode());
        assertEquals(productDetail, shipping.getProductDetail());
    }

    @Test
    void testFreeShipping_BooleanValues() {
        // Test true value
        shipping.setFreeShipping(true);
        assertTrue(shipping.isFreeShipping());

        // Test false value
        shipping.setFreeShipping(false);
        assertFalse(shipping.isFreeShipping());
    }

    @Test
    void testProductDetailRelationship_ShouldMaintainBidirectionalReference() {
        // When
        shipping.setProductDetail(productDetail);

        // Then
        assertEquals(productDetail, shipping.getProductDetail());
        assertEquals("MLA123456789", shipping.getProductDetail().getProductId());
    }

    @Test
    void testEqualsAndHashCode_WithLombok() {
        // Given
        Shipping shipping1 = new Shipping();
        shipping1.setId(1L);
        shipping1.setFreeShipping(true);
        shipping1.setLogisticType("fulfillment");
        shipping1.setMode("me2");

        Shipping shipping2 = new Shipping();
        shipping2.setId(1L);
        shipping2.setFreeShipping(true);
        shipping2.setLogisticType("fulfillment");
        shipping2.setMode("me2");

        Shipping shipping3 = new Shipping();
        shipping3.setId(2L);
        shipping3.setFreeShipping(false);
        shipping3.setLogisticType("cross_docking");
        shipping3.setMode("custom");

        // Then
        assertEquals(shipping1, shipping2);
        assertNotEquals(shipping1, shipping3);
        assertEquals(shipping1.hashCode(), shipping2.hashCode());
        assertNotEquals(shipping1.hashCode(), shipping3.hashCode());
    }

    @Test
    void testToString_ShouldContainFields() {
        // Given
        shipping.setId(1L);
        shipping.setFreeShipping(true);
        shipping.setLogisticType("fulfillment");
        shipping.setMode("me2");

        // When
        String toString = shipping.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Shipping"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("freeShipping=true"));
        assertTrue(toString.contains("logisticType=fulfillment"));
        assertTrue(toString.contains("mode=me2"));
    }

    @Test
    void testNullValues_ShouldHandleGracefully() {
        // When
        shipping.setLogisticType(null);
        shipping.setMode(null);
        shipping.setProductDetail(null);

        // Then
        assertNull(shipping.getLogisticType());
        assertNull(shipping.getMode());
        assertNull(shipping.getProductDetail());
        // freeShipping should still be false (primitive boolean default)
        assertFalse(shipping.isFreeShipping());
    }

    @Test
    void testEmptyStrings_ShouldHandleCorrectly() {
        // When
        shipping.setLogisticType("");
        shipping.setMode("");

        // Then
        assertEquals("", shipping.getLogisticType());
        assertEquals("", shipping.getMode());
    }

    @Test
    void testCommonShippingModes_ShouldHandleCorrectly() {
        // Test different shipping modes
        String[] modes = {"me2", "custom", "not_specified"};
        String[] logisticTypes = {"fulfillment", "cross_docking", "drop_off"};

        for (String mode : modes) {
            shipping.setMode(mode);
            assertEquals(mode, shipping.getMode());
        }

        for (String logisticType : logisticTypes) {
            shipping.setLogisticType(logisticType);
            assertEquals(logisticType, shipping.getLogisticType());
        }
    }
}