package com.meli.product_detail.services;

import com.meli.product_detail.entities.ProductDetail;
import com.meli.product_detail.exceptions.MeliException;
import com.meli.product_detail.repositories.ProductDetailRepositories;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductDetailServiceImpl.
 * 
 * Valida la lógica de negocio, manejo de transacciones,
 * trazabilidad distribuida y casos de error.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductDetailServiceImplTest {

    @Mock
    private ProductDetailRepositories repositories;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    @Mock
    private TraceContext traceContext;

    @InjectMocks
    private ProductDetailServiceImpl service;

    private ProductDetail sampleProductDetail;
    private List<ProductDetail> sampleProductList;

    @BeforeEach
    void setUp() {
        // Configurar mock de Span y TraceContext
        when(tracer.nextSpan()).thenReturn(span);
        when(span.name(anyString())).thenReturn(span);
        when(span.start()).thenReturn(span);
        when(span.context()).thenReturn(traceContext);
        when(traceContext.traceId()).thenReturn("test-trace-id-123456789");
        
        // Crear datos de prueba
        setupTestData();
    }

    private void setupTestData() {
        sampleProductDetail = new ProductDetail();
        sampleProductDetail.setId(1L);
        sampleProductDetail.setProductId("MLA123456789");
        sampleProductDetail.setTitle("iPhone 13 Pro 128GB");
        sampleProductDetail.setCondition("new");
        sampleProductDetail.setCategoryId("MLA1055");
        sampleProductDetail.setPrice(new BigDecimal("599999.99"));
        sampleProductDetail.setCurrencyId("ARS");
        sampleProductDetail.setAvailableQuantity(10);
        sampleProductDetail.setSoldQuantity(5);
        sampleProductDetail.setStatus("active");
        sampleProductDetail.setPermalink("https://articulo.mercadolibre.com.ar/MLA123456789");
        sampleProductDetail.setThumbnail("https://http2.mlstatic.com/D_123456-MLA.jpg");

        ProductDetail secondProduct = new ProductDetail();
        secondProduct.setId(2L);
        secondProduct.setProductId("MLA987654321");
        secondProduct.setTitle("Samsung Galaxy S21");
        secondProduct.setCondition("new");
        secondProduct.setPrice(new BigDecimal("450000.00"));
        secondProduct.setCurrencyId("ARS");
        secondProduct.setAvailableQuantity(15);

        sampleProductList = Arrays.asList(sampleProductDetail, secondProduct);
    }

    @Test
    void getAllProductDetails_Success_ShouldReturnListOfProducts() throws MeliException {
        // Given
        when(repositories.findAll()).thenReturn(sampleProductList);

        // When
        List<ProductDetail> result = service.getAllProductDetails();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA123456789", result.get(0).getProductId());
        assertEquals("iPhone 13 Pro 128GB", result.get(0).getTitle());
        assertEquals("MLA987654321", result.get(1).getProductId());
        assertEquals("Samsung Galaxy S21", result.get(1).getTitle());

        // Verify interactions
        verify(repositories, times(1)).findAll();
        verify(tracer, times(1)).nextSpan();
        verify(span, times(1)).name("ProductDetailServiceImpl.getAllProductDetails");
        verify(span, times(1)).start();
        verify(span, times(1)).end();
    }

    @Test
    void getAllProductDetails_EmptyList_ShouldReturnEmptyList() throws MeliException {
        // Given
        when(repositories.findAll()).thenReturn(Arrays.asList());

        // When
        List<ProductDetail> result = service.getAllProductDetails();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(repositories, times(1)).findAll();
        verify(span, times(1)).end();
    }

    @Test
    void getAllProductDetails_RepositoryException_ShouldThrowMeliException() {
        // Given
        RuntimeException repositoryException = new RuntimeException("Database connection error");
        when(repositories.findAll()).thenThrow(repositoryException);

        // When & Then
        MeliException exception = assertThrows(MeliException.class, () -> {
            service.getAllProductDetails();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals("001001", exception.getErrorCode());
        assertNull(exception.getRelatedObject());

        verify(repositories, times(1)).findAll();
        verify(span, times(1)).end();
    }

    @Test
    void getProductDetailByProductId_Success_ShouldReturnProduct() throws MeliException {
        // Given
        String productId = "MLA123456789";
        when(repositories.findByProductId(productId)).thenReturn(Optional.of(sampleProductDetail));

        // When
        ProductDetail result = service.getProductDetailByProductId(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("iPhone 13 Pro 128GB", result.getTitle());
        assertEquals("new", result.getCondition());
        assertEquals(new BigDecimal("599999.99"), result.getPrice());
        assertEquals("ARS", result.getCurrencyId());
        assertEquals(10, result.getAvailableQuantity());

        verify(repositories, times(1)).findByProductId(productId);
        verify(tracer, times(1)).nextSpan();
        verify(span, times(1)).name("ProductDetailServiceImpl.getProductDetailByProductId");
        verify(span, times(1)).start();
        verify(span, times(1)).end();
    }

    @Test
    void getProductDetailByProductId_NotFound_ShouldThrowMeliException() {
        // Given
        String productId = "MLA999999999";
        when(repositories.findByProductId(productId)).thenReturn(Optional.empty());

        // When & Then
        MeliException exception = assertThrows(MeliException.class, () -> {
            service.getProductDetailByProductId(productId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("001002", exception.getErrorCode());
        assertEquals(productId, exception.getRelatedObject());
        assertTrue(exception.getMessage().contains("001002"));
        assertTrue(exception.getMessage().contains("not found"));

        verify(repositories, times(1)).findByProductId(productId);
        verify(span, times(1)).end();
    }

    @Test
    void getProductDetailByProductId_RepositoryException_ShouldThrowMeliException() {
        // Given
        String productId = "MLA123456789";
        RuntimeException repositoryException = new RuntimeException("Database timeout");
        when(repositories.findByProductId(productId)).thenThrow(repositoryException);

        // When & Then
        MeliException exception = assertThrows(MeliException.class, () -> {
            service.getProductDetailByProductId(productId);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals("001003", exception.getErrorCode());
        assertNull(exception.getRelatedObject());

        verify(repositories, times(1)).findByProductId(productId);
        verify(span, times(1)).end();
    }

    @Test
    void getProductDetailByProductId_NullProductId_ShouldHandleGracefully() {
        // Given
        String productId = null;
        when(repositories.findByProductId(productId)).thenReturn(Optional.empty());

        // When & Then
        MeliException exception = assertThrows(MeliException.class, () -> {
            service.getProductDetailByProductId(productId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("001002", exception.getErrorCode());

        verify(repositories, times(1)).findByProductId(productId);
    }

    @Test
    void getProductDetailByProductId_EmptyProductId_ShouldHandleGracefully() {
        // Given
        String productId = "";
        when(repositories.findByProductId(productId)).thenReturn(Optional.empty());

        // When & Then
        MeliException exception = assertThrows(MeliException.class, () -> {
            service.getProductDetailByProductId(productId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("001002", exception.getErrorCode());

        verify(repositories, times(1)).findByProductId(productId);
    }

    @Test
    void testTracingIntegration_ShouldCreateAndManageSpansCorrectly() throws MeliException {
        // Given
        when(repositories.findAll()).thenReturn(sampleProductList);

        // When
        service.getAllProductDetails();

        // Then - Verify tracing interactions
        verify(tracer, times(1)).nextSpan();
        verify(span, times(1)).name("ProductDetailServiceImpl.getAllProductDetails");
        verify(span, times(1)).start();
        verify(span, times(1)).end();

        // Verify no error tags are added for successful operation
        verify(span, never()).tag(eq("error"), anyString());
    }

    @Test
    void testTransactionalAnnotation_ShouldBePresent() {
        // Verify that the service class has @Transactional annotation
        var serviceClass = ProductDetailServiceImpl.class;
        var transactionalAnnotation = serviceClass.getAnnotation(jakarta.transaction.Transactional.class);
        
        assertNotNull(transactionalAnnotation, "Service should have @Transactional annotation");
    }

    @Test
    void getAllProductDetails_LargeDataset_ShouldHandleCorrectly() throws MeliException {
        // Given - Create a large list of products
        List<ProductDetail> largeProductList = createLargeProductList(1000);
        when(repositories.findAll()).thenReturn(largeProductList);

        // When
        List<ProductDetail> result = service.getAllProductDetails();

        // Then
        assertNotNull(result);
        assertEquals(1000, result.size());
        verify(repositories, times(1)).findAll();
    }

    private List<ProductDetail> createLargeProductList(int size) {
        List<ProductDetail> products = new java.util.ArrayList<>();
        for (int i = 0; i < size; i++) {
            ProductDetail product = new ProductDetail();
            product.setId((long) (i + 1));
            product.setProductId("MLA" + String.format("%09d", i + 1));
            product.setTitle("Product " + (i + 1));
            product.setCondition("new");
            product.setPrice(new BigDecimal("100.00").multiply(new BigDecimal(i + 1)));
            products.add(product);
        }
        return products;
    }

    @Test
    void getProductDetailByProductId_WithSpecialCharacters_ShouldWork() throws MeliException {
        // Given
        String productIdWithSpecialChars = "MLA123-456_789";
        ProductDetail productWithSpecialId = new ProductDetail();
        productWithSpecialId.setProductId(productIdWithSpecialChars);
        productWithSpecialId.setTitle("Special Product");
        
        when(repositories.findByProductId(productIdWithSpecialChars))
                .thenReturn(Optional.of(productWithSpecialId));

        // When
        ProductDetail result = service.getProductDetailByProductId(productIdWithSpecialChars);

        // Then
        assertNotNull(result);
        assertEquals(productIdWithSpecialChars, result.getProductId());
        assertEquals("Special Product", result.getTitle());

        verify(repositories, times(1)).findByProductId(productIdWithSpecialChars);
    }
}