package com.meli.product_detail.repositories;

import com.meli.product_detail.entities.ProductDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para ProductDetailRepositories.
 * 
 * Utiliza @DataJpaTest para probar las operaciones de base de datos
 * con una base de datos H2 en memoria.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@DataJpaTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "spring.liquibase.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProductDetailRepositoriesTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductDetailRepositories repository;

    @Test
    void testFindByProductId_Success_ShouldReturnProduct() {
        // Given
        ProductDetail product = createSampleProduct("MLA123456789");
        entityManager.persistAndFlush(product);

        // When
        Optional<ProductDetail> result = repository.findByProductId("MLA123456789");

        // Then
        assertTrue(result.isPresent());
        assertEquals("MLA123456789", result.get().getProductId());
        assertEquals("iPhone 13 Pro 128GB", result.get().getTitle());
        assertEquals("new", result.get().getCondition());
        assertEquals(new BigDecimal("599999.99"), result.get().getPrice());
    }

    @Test
    void testFindByProductId_NotFound_ShouldReturnEmpty() {
        // Given - No data persisted

        // When
        Optional<ProductDetail> result = repository.findByProductId("MLA999999999");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByProductId_NullInput_ShouldReturnEmpty() {
        // When
        Optional<ProductDetail> result = repository.findByProductId(null);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByProductId_EmptyString_ShouldReturnEmpty() {
        // When
        Optional<ProductDetail> result = repository.findByProductId("");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll_WithMultipleProducts_ShouldReturnAllProducts() {
        // Given
        ProductDetail product1 = createSampleProduct("MLA987654321");
        ProductDetail product2 = createSampleProduct("MLA234567890");
        product2.setTitle("Samsung Galaxy S21");
        product2.setPrice(new BigDecimal("450000.00"));
        
        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);

        // When
        List<ProductDetail> result = repository.findAll();

        // Then
        assertEquals(2, result.size());
        
        // Verify both products are present
        assertTrue(result.stream().anyMatch(p -> "MLA987654321".equals(p.getProductId())));
        assertTrue(result.stream().anyMatch(p -> "MLA234567890".equals(p.getProductId())));
        assertTrue(result.stream().anyMatch(p -> "iPhone 13 Pro 128GB".equals(p.getTitle())));
        assertTrue(result.stream().anyMatch(p -> "Samsung Galaxy S21".equals(p.getTitle())));
    }

    @Test
    void testFindAll_EmptyDatabase_ShouldReturnEmptyList() {
        // Given - No data persisted

        // When
        List<ProductDetail> result = repository.findAll();

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testSave_NewProduct_ShouldPersistSuccessfully() {
        // Given
        ProductDetail product = createSampleProduct("MLA555666777");

        // When
        ProductDetail savedProduct = repository.save(product);

        // Then
        assertNotNull(savedProduct.getId());
        assertEquals("MLA555666777", savedProduct.getProductId());
        assertEquals("iPhone 13 Pro 128GB", savedProduct.getTitle());
        
        // Verify it's actually persisted
        entityManager.flush();
        ProductDetail foundProduct = entityManager.find(ProductDetail.class, savedProduct.getId());
        assertNotNull(foundProduct);
        assertEquals("MLA555666777", foundProduct.getProductId());
    }

    @Test
    void testDelete_ExistingProduct_ShouldRemoveSuccessfully() {
        // Given
        ProductDetail product = createSampleProduct("MLA111222333");
        ProductDetail savedProduct = entityManager.persistAndFlush(product);

        // When
        repository.delete(savedProduct);
        entityManager.flush();

        // Then
        ProductDetail deletedProduct = entityManager.find(ProductDetail.class, savedProduct.getId());
        assertNull(deletedProduct);
        
        // Verify via repository as well
        Optional<ProductDetail> result = repository.findByProductId("MLA111222333");
        assertTrue(result.isEmpty());
    }

    @Test
    void testCount_WithMultipleProducts_ShouldReturnCorrectCount() {
        // Given
        entityManager.persistAndFlush(createSampleProduct("MLA111111111"));
        entityManager.persistAndFlush(createSampleProduct("MLA222222222"));
        entityManager.persistAndFlush(createSampleProduct("MLA333333333"));

        // When
        long count = repository.count();

        // Then
        assertEquals(3, count);
    }

    @Test
    void testExistsById_ExistingProduct_ShouldReturnTrue() {
        // Given
        ProductDetail product = createSampleProduct("MLA444555666");
        ProductDetail savedProduct = entityManager.persistAndFlush(product);

        // When
        boolean exists = repository.existsById(savedProduct.getId().intValue());

        // Then
        assertTrue(exists);
    }

    @Test
    void testExistsById_NonExistingProduct_ShouldReturnFalse() {
        // When
        boolean exists = repository.existsById(999999);

        // Then
        assertFalse(exists);
    }

    @Test
    void testFindById_ExistingProduct_ShouldReturnProduct() {
        // Given
        ProductDetail product = createSampleProduct("MLA777888999");
        ProductDetail savedProduct = entityManager.persistAndFlush(product);

        // When
        Optional<ProductDetail> result = repository.findById(savedProduct.getId().intValue());

        // Then
        assertTrue(result.isPresent());
        assertEquals("MLA777888999", result.get().getProductId());
        assertEquals(savedProduct.getId(), result.get().getId());
    }

    @Test
    void testUpdate_ExistingProduct_ShouldUpdateSuccessfully() {
        // Given
        ProductDetail product = createSampleProduct("MLA888999000");
        ProductDetail savedProduct = entityManager.persistAndFlush(product);
        
        // Modify the product
        savedProduct.setTitle("Updated iPhone Title");
        savedProduct.setPrice(new BigDecimal("699999.99"));
        savedProduct.setAvailableQuantity(20);

        // When
        ProductDetail updatedProduct = repository.save(savedProduct);
        entityManager.flush();

        // Then
        assertEquals(savedProduct.getId(), updatedProduct.getId());
        assertEquals("Updated iPhone Title", updatedProduct.getTitle());
        assertEquals(new BigDecimal("699999.99"), updatedProduct.getPrice());
        assertEquals(20, updatedProduct.getAvailableQuantity());
        
        // Verify persistence
        ProductDetail foundProduct = entityManager.find(ProductDetail.class, savedProduct.getId());
        assertEquals("Updated iPhone Title", foundProduct.getTitle());
        assertEquals(new BigDecimal("699999.99"), foundProduct.getPrice());
    }

    @Test
    void testFindByProductId_CaseSensitive_ShouldWork() {
        // Given
        ProductDetail product = createSampleProduct("MLA345678901");
        entityManager.persistAndFlush(product);

        // When - Test with exact case
        Optional<ProductDetail> result1 = repository.findByProductId("MLA345678901");
        // Test with different case (should not find in case-sensitive database)
        Optional<ProductDetail> result2 = repository.findByProductId("mla345678901");

        // Then
        assertTrue(result1.isPresent());
        // In H2 default configuration, it's case-sensitive
        assertTrue(result2.isEmpty());
    }

    @Test
    void testFindByProductId_SpecialCharacters_ShouldWork() {
        // Given
        ProductDetail product = createSampleProduct("MLA123-456_789");
        entityManager.persistAndFlush(product);

        // When
        Optional<ProductDetail> result = repository.findByProductId("MLA123-456_789");

        // Then
        assertTrue(result.isPresent());
        assertEquals("MLA123-456_789", result.get().getProductId());
    }

    @Test
    void testRepositoryInheritance_ShouldHaveJpaRepositoryMethods() {
        // This test verifies that our repository correctly extends JpaRepository
        // and has access to all standard CRUD operations
        
        // Given
        ProductDetail product = createSampleProduct("MLA000111222");
        
        // When & Then - Test inherited methods are available
        assertDoesNotThrow(() -> {
            repository.save(product);
            repository.findAll();
            repository.count();
            repository.existsById(1);
            repository.findById(1);
            repository.deleteById(1);
        });
    }

    /**
     * Helper method to create sample ProductDetail for testing
     */
    private ProductDetail createSampleProduct(String productId) {
        ProductDetail product = new ProductDetail();
        // Don't set ID - let it be auto-generated
        product.setProductId(productId);
        product.setTitle("iPhone 13 Pro 128GB");
        product.setCondition("new");
        product.setCategoryId("MLA1055");
        product.setListingTypeId("gold_special");
        product.setSiteId("MLA");
        product.setPrice(new BigDecimal("599999.99"));
        product.setCurrencyId("ARS");
        product.setAvailableQuantity(10);
        product.setSoldQuantity(5);
        product.setBuyingMode("buy_it_now");
        product.setStatus("active");
        product.setPermalink("https://articulo.mercadolibre.com.ar/" + productId);
        product.setThumbnail("https://http2.mlstatic.com/D_123456-MLA.jpg");
        
        return product;
    }
}