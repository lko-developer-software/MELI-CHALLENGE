package com.meli.product_detail.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad SellerAddress.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
class SellerAddressTest {

    private SellerAddress sellerAddress;
    private Seller seller;

    @BeforeEach
    void setUp() {
        sellerAddress = new SellerAddress();
        seller = new Seller();
        seller.setId(1L);
        seller.setNickname("VENDEDOR123");
    }

    @Test
    void testSellerAddressCreation_ShouldCreateWithDefaultValues() {
        // Then
        assertNotNull(sellerAddress);
        assertNull(sellerAddress.getId());
        assertNull(sellerAddress.getCity());
        assertNull(sellerAddress.getState());
        assertNull(sellerAddress.getCountry());
        assertNull(sellerAddress.getZipCode());
        assertNull(sellerAddress.getSeller());
    }

    @Test
    void testSettersAndGetters_ShouldWorkCorrectly() {
        // Given
        Long expectedId = 1L;
        String expectedCity = "Buenos Aires";
        String expectedState = "Buenos Aires";
        String expectedCountry = "Argentina";
        String expectedZipCode = "C1425";

        // When
        sellerAddress.setId(expectedId);
        sellerAddress.setCity(expectedCity);
        sellerAddress.setState(expectedState);
        sellerAddress.setCountry(expectedCountry);
        sellerAddress.setZipCode(expectedZipCode);
        sellerAddress.setSeller(seller);

        // Then
        assertEquals(expectedId, sellerAddress.getId());
        assertEquals(expectedCity, sellerAddress.getCity());
        assertEquals(expectedState, sellerAddress.getState());
        assertEquals(expectedCountry, sellerAddress.getCountry());
        assertEquals(expectedZipCode, sellerAddress.getZipCode());
        assertEquals(seller, sellerAddress.getSeller());
    }

    @Test
    void testSellerRelationship_ShouldMaintainBidirectionalReference() {
        // When
        sellerAddress.setSeller(seller);

        // Then
        assertEquals(seller, sellerAddress.getSeller());
        assertEquals("VENDEDOR123", sellerAddress.getSeller().getNickname());
    }

    @Test
    void testEqualsAndHashCode_WithLombok() {
        // Given
        SellerAddress address1 = new SellerAddress();
        address1.setId(1L);
        address1.setCity("Buenos Aires");
        address1.setState("Buenos Aires");
        address1.setCountry("Argentina");
        address1.setZipCode("C1425");

        SellerAddress address2 = new SellerAddress();
        address2.setId(1L);
        address2.setCity("Buenos Aires");
        address2.setState("Buenos Aires");
        address2.setCountry("Argentina");
        address2.setZipCode("C1425");

        SellerAddress address3 = new SellerAddress();
        address3.setId(2L);
        address3.setCity("Córdoba");
        address3.setState("Córdoba");
        address3.setCountry("Argentina");
        address3.setZipCode("X5000");

        // Then
        assertEquals(address1, address2);
        assertNotEquals(address1, address3);
        assertEquals(address1.hashCode(), address2.hashCode());
        assertNotEquals(address1.hashCode(), address3.hashCode());
    }

    @Test
    void testToString_ShouldContainFields() {
        // Given
        sellerAddress.setId(1L);
        sellerAddress.setCity("Buenos Aires");
        sellerAddress.setState("Buenos Aires");
        sellerAddress.setCountry("Argentina");
        sellerAddress.setZipCode("C1425");

        // When
        String toString = sellerAddress.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("SellerAddress"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("city=Buenos Aires"));
        assertTrue(toString.contains("state=Buenos Aires"));
        assertTrue(toString.contains("country=Argentina"));
        assertTrue(toString.contains("zipCode=C1425"));
    }

    @Test
    void testArgentinianAddresses_ShouldHandleCorrectly() {
        // Test Buenos Aires
        sellerAddress.setCity("Buenos Aires");
        sellerAddress.setState("Buenos Aires");
        sellerAddress.setCountry("Argentina");
        sellerAddress.setZipCode("C1425");

        assertEquals("Buenos Aires", sellerAddress.getCity());
        assertEquals("Buenos Aires", sellerAddress.getState());
        assertEquals("Argentina", sellerAddress.getCountry());
        assertEquals("C1425", sellerAddress.getZipCode());

        // Test Córdoba
        sellerAddress.setCity("Córdoba");
        sellerAddress.setState("Córdoba");
        sellerAddress.setZipCode("X5000");

        assertEquals("Córdoba", sellerAddress.getCity());
        assertEquals("Córdoba", sellerAddress.getState());
        assertEquals("X5000", sellerAddress.getZipCode());
    }

    @Test
    void testInternationalAddresses_ShouldHandleCorrectly() {
        // Test Mexican address
        sellerAddress.setCity("Ciudad de México");
        sellerAddress.setState("Ciudad de México");
        sellerAddress.setCountry("México");
        sellerAddress.setZipCode("01000");

        assertEquals("Ciudad de México", sellerAddress.getCity());
        assertEquals("Ciudad de México", sellerAddress.getState());
        assertEquals("México", sellerAddress.getCountry());
        assertEquals("01000", sellerAddress.getZipCode());

        // Test Colombian address
        sellerAddress.setCity("Bogotá");
        sellerAddress.setState("Cundinamarca");
        sellerAddress.setCountry("Colombia");
        sellerAddress.setZipCode("110111");

        assertEquals("Bogotá", sellerAddress.getCity());
        assertEquals("Cundinamarca", sellerAddress.getState());
        assertEquals("Colombia", sellerAddress.getCountry());
        assertEquals("110111", sellerAddress.getZipCode());
    }

    @Test
    void testZipCodeValidation_ShouldHandleDifferentFormats() {
        // Test different zip code formats
        String[] zipCodes = {
            "C1425",      // Buenos Aires format
            "X5000",      // Córdoba format
            "01000",      // Mexican format
            "110111",     // Colombian format
            "12345",      // US format
            "SW1A1AA",    // UK format
            "75001"       // French format
        };

        for (String zipCode : zipCodes) {
            sellerAddress.setZipCode(zipCode);
            assertEquals(zipCode, sellerAddress.getZipCode());
        }
    }

    @Test
    void testNullValues_ShouldHandleGracefully() {
        // When
        sellerAddress.setCity(null);
        sellerAddress.setState(null);
        sellerAddress.setCountry(null);
        sellerAddress.setZipCode(null);
        sellerAddress.setSeller(null);

        // Then
        assertNull(sellerAddress.getCity());
        assertNull(sellerAddress.getState());
        assertNull(sellerAddress.getCountry());
        assertNull(sellerAddress.getZipCode());
        assertNull(sellerAddress.getSeller());
    }

    @Test
    void testEmptyStrings_ShouldHandleCorrectly() {
        // When
        sellerAddress.setCity("");
        sellerAddress.setState("");
        sellerAddress.setCountry("");
        sellerAddress.setZipCode("");

        // Then
        assertEquals("", sellerAddress.getCity());
        assertEquals("", sellerAddress.getState());
        assertEquals("", sellerAddress.getCountry());
        assertEquals("", sellerAddress.getZipCode());
    }

    @Test
    void testSpecialCharactersInAddress_ShouldHandleCorrectly() {
        // Given
        String cityWithAccents = "São Paulo";
        String stateWithAccents = "São Paulo";
        String countryWithAccents = "Brasil";

        // When
        sellerAddress.setCity(cityWithAccents);
        sellerAddress.setState(stateWithAccents);
        sellerAddress.setCountry(countryWithAccents);

        // Then
        assertEquals(cityWithAccents, sellerAddress.getCity());
        assertEquals(stateWithAccents, sellerAddress.getState());
        assertEquals(countryWithAccents, sellerAddress.getCountry());
    }

    @Test
    void testLongAddressFields_ShouldHandleCorrectly() {
        // Given
        String longCity = "A Very Long City Name With Many Words For Testing Purposes Only";
        String longState = "A Very Long State Name With Many Words For Testing Purposes Only";
        String longCountry = "A Very Long Country Name With Many Words For Testing Purposes Only";

        // When
        sellerAddress.setCity(longCity);
        sellerAddress.setState(longState);
        sellerAddress.setCountry(longCountry);

        // Then
        assertEquals(longCity, sellerAddress.getCity());
        assertEquals(longState, sellerAddress.getState());
        assertEquals(longCountry, sellerAddress.getCountry());
    }

    @Test
    void testBidirectionalRelationshipWithSeller_ShouldWork() {
        // When
        sellerAddress.setSeller(seller);
        seller.setAddress(sellerAddress);

        // Then
        assertEquals(seller, sellerAddress.getSeller());
        assertEquals(sellerAddress, seller.getAddress());
        
        // Verify we can navigate both ways
        assertEquals("VENDEDOR123", sellerAddress.getSeller().getNickname());
        assertEquals(sellerAddress.getId(), seller.getAddress().getId());
    }
}