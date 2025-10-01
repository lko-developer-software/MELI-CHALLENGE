package com.meli.product_detail.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Seller.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
class SellerTest {

    private Seller seller;
    private ProductDetail productDetail;
    private SellerAddress sellerAddress;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        productDetail = new ProductDetail();
        productDetail.setId(1L);
        productDetail.setProductId("MLA123456789");
        
        sellerAddress = new SellerAddress();
        sellerAddress.setId(1L);
        sellerAddress.setCity("Buenos Aires");
        sellerAddress.setCountry("Argentina");
    }

    @Test
    void testSellerCreation_ShouldCreateWithDefaultValues() {
        // Then
        assertNotNull(seller);
        assertNull(seller.getId());
        assertNull(seller.getNickname());
        assertNull(seller.getSellerType());
        assertNull(seller.getProductDetail());
        assertNull(seller.getAddress());
    }

    @Test
    void testSettersAndGetters_ShouldWorkCorrectly() {
        // Given
        Long expectedId = 1L;
        String expectedNickname = "VENDEDOR123";
        String expectedSellerType = "professional";

        // When
        seller.setId(expectedId);
        seller.setNickname(expectedNickname);
        seller.setSellerType(expectedSellerType);
        seller.setProductDetail(productDetail);
        seller.setAddress(sellerAddress);

        // Then
        assertEquals(expectedId, seller.getId());
        assertEquals(expectedNickname, seller.getNickname());
        assertEquals(expectedSellerType, seller.getSellerType());
        assertEquals(productDetail, seller.getProductDetail());
        assertEquals(sellerAddress, seller.getAddress());
    }

    @Test
    void testProductDetailRelationship_ShouldMaintainBidirectionalReference() {
        // When
        seller.setProductDetail(productDetail);

        // Then
        assertEquals(productDetail, seller.getProductDetail());
        assertEquals("MLA123456789", seller.getProductDetail().getProductId());
    }

    @Test
    void testAddressRelationship_ShouldMaintainOneToOneReference() {
        // When
        seller.setAddress(sellerAddress);
        sellerAddress.setSeller(seller);

        // Then
        assertEquals(sellerAddress, seller.getAddress());
        assertEquals(seller, sellerAddress.getSeller());
        assertEquals("Buenos Aires", seller.getAddress().getCity());
    }

    @Test
    void testEqualsAndHashCode_WithLombok() {
        // Given
        Seller seller1 = new Seller();
        seller1.setId(1L);
        seller1.setNickname("VENDEDOR123");
        seller1.setSellerType("professional");

        Seller seller2 = new Seller();
        seller2.setId(1L);
        seller2.setNickname("VENDEDOR123");
        seller2.setSellerType("professional");

        Seller seller3 = new Seller();
        seller3.setId(2L);
        seller3.setNickname("VENDEDOR456");
        seller3.setSellerType("basic");

        // Then
        assertEquals(seller1, seller2);
        assertNotEquals(seller1, seller3);
        assertEquals(seller1.hashCode(), seller2.hashCode());
        assertNotEquals(seller1.hashCode(), seller3.hashCode());
    }

    @Test
    void testToString_ShouldContainFields() {
        // Given
        seller.setId(1L);
        seller.setNickname("VENDEDOR123");
        seller.setSellerType("professional");

        // When
        String toString = seller.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Seller"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nickname=VENDEDOR123"));
        assertTrue(toString.contains("sellerType=professional"));
    }

    @Test
    void testSellerTypes_ShouldHandleValidTypes() {
        // Test different seller types
        String[] sellerTypes = {"professional", "basic", "premium"};

        for (String type : sellerTypes) {
            seller.setSellerType(type);
            assertEquals(type, seller.getSellerType());
        }
    }

    @Test
    void testNullValues_ShouldHandleGracefully() {
        // When
        seller.setNickname(null);
        seller.setSellerType(null);
        seller.setProductDetail(null);
        seller.setAddress(null);

        // Then
        assertNull(seller.getNickname());
        assertNull(seller.getSellerType());
        assertNull(seller.getProductDetail());
        assertNull(seller.getAddress());
    }

    @Test
    void testEmptyStrings_ShouldHandleCorrectly() {
        // When
        seller.setNickname("");
        seller.setSellerType("");

        // Then
        assertEquals("", seller.getNickname());
        assertEquals("", seller.getSellerType());
    }

    @Test
    void testNicknameVariations_ShouldHandleCorrectly() {
        // Test different nickname formats
        String[] nicknames = {
            "VENDEDOR123",
            "vendedor_abc",
            "Vendedor-XYZ",
            "SUPER.VENDEDOR",
            "123VENDEDOR456"
        };

        for (String nickname : nicknames) {
            seller.setNickname(nickname);
            assertEquals(nickname, seller.getNickname());
        }
    }

    @Test
    void testLongNickname_ShouldHandleCorrectly() {
        // Given
        String longNickname = "VERY_LONG_SELLER_NICKNAME_WITH_MANY_CHARACTERS_FOR_TESTING_PURPOSES";

        // When
        seller.setNickname(longNickname);

        // Then
        assertEquals(longNickname, seller.getNickname());
    }

    @Test
    void testCascadeRelationshipWithAddress_ShouldSetBidirectional() {
        // Given
        SellerAddress address = new SellerAddress();
        address.setCity("Córdoba");
        address.setState("Córdoba");
        address.setCountry("Argentina");
        address.setZipCode("X5000");

        // When
        seller.setAddress(address);
        address.setSeller(seller);

        // Then
        assertNotNull(seller.getAddress());
        assertEquals("Córdoba", seller.getAddress().getCity());
        assertEquals("X5000", seller.getAddress().getZipCode());
        assertEquals(seller, address.getSeller());
    }
}