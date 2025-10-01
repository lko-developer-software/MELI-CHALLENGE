package com.meli.product_detail.controllers;

import com.meli.product_detail.entities.ProductDetail;
import com.meli.product_detail.exceptions.MeliException;
import com.meli.product_detail.services.ProductDetailService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para ProductDetailControllers.
 * 
 * Valida el comportamiento de los endpoints REST, manejo de errores,
 * trazabilidad distribuida y métricas.
 * 
 * @author Osneider Manuel Acevedo Naranjo
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductDetailControllersTest {

    @Mock
    private ProductDetailService productDetailService;

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    @Mock
    private TraceContext traceContext;

    @InjectMocks
    private ProductDetailControllers controller;

    private MockMvc mockMvc;
    private ProductDetail sampleProductDetail;
    private List<ProductDetail> sampleProductList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
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
    void getAllProductDetails_Success_ShouldReturnListOfProducts() throws Exception {
        // Given
        when(productDetailService.getAllProductDetails()).thenReturn(sampleProductList);

        // When & Then
        mockMvc.perform(get("/detail")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value("MLA123456789"))
                .andExpect(jsonPath("$[0].title").value("iPhone 13 Pro 128GB"))
                .andExpect(jsonPath("$[0].condition").value("new"))
                .andExpect(jsonPath("$[0].price").value(599999.99))
                .andExpect(jsonPath("$[1].productId").value("MLA987654321"))
                .andExpect(jsonPath("$[1].title").value("Samsung Galaxy S21"));

        // Verify interactions
        verify(productDetailService, times(1)).getAllProductDetails();
        verify(tracer, times(1)).nextSpan();
        verify(span, times(1)).name("ProductDetailControllers.getAllProductDetails");
        verify(span, times(1)).start();
        verify(span, times(1)).end();
    }

    @Test
    void getAllProductDetails_EmptyList_ShouldReturnEmptyArray() throws Exception {
        // Given
        when(productDetailService.getAllProductDetails()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/detail")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(productDetailService, times(1)).getAllProductDetails();
    }

    @Test
    void getAllProductDetails_ServiceException_ShouldReturnInternalServerError() throws Exception {
        // Given
        MeliException exception = new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "001001");
        when(productDetailService.getAllProductDetails()).thenThrow(exception);

        // When & Then
        mockMvc.perform(get("/detail")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("001001")));

        verify(productDetailService, times(1)).getAllProductDetails();
        verify(span, times(1)).end();
    }

    @Test
    void getProductDetailByProductId_Success_ShouldReturnProduct() throws Exception {
        // Given
        String productId = "MLA123456789";
        when(productDetailService.getProductDetailByProductId(productId)).thenReturn(sampleProductDetail);

        // When & Then
        mockMvc.perform(get("/detail/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(productId))
                .andExpect(jsonPath("$.title").value("iPhone 13 Pro 128GB"))
                .andExpect(jsonPath("$.condition").value("new"))
                .andExpect(jsonPath("$.price").value(599999.99))
                .andExpect(jsonPath("$.currencyId").value("ARS"))
                .andExpect(jsonPath("$.availableQuantity").value(10))
                .andExpect(jsonPath("$.soldQuantity").value(5));

        verify(productDetailService, times(1)).getProductDetailByProductId(productId);
        verify(tracer, times(1)).nextSpan();
        verify(span, times(1)).name("ProductDetailControllers.getProductDetailByProductId");
        verify(span, times(1)).start();
        verify(span, times(1)).end();
    }

    @Test
    void getProductDetailByProductId_NotFound_ShouldReturnNotFoundError() throws Exception {
        // Given
        String productId = "MLA999999999";
        MeliException exception = new MeliException(HttpStatus.NOT_FOUND, productId, "001002");
        when(productDetailService.getProductDetailByProductId(productId)).thenThrow(exception);

        // When & Then
        mockMvc.perform(get("/detail/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("001002")))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(productId)))
                .andExpect(jsonPath("$.path").value("/detail/" + productId))
                .andExpect(jsonPath("$.trace").value("test-trace-id-123456789"));

        verify(productDetailService, times(1)).getProductDetailByProductId(productId);
        verify(span, times(1)).end();
    }

    @Test
    void getProductDetailByProductId_InternalServerError_ShouldReturnInternalServerError() throws Exception {
        // Given
        String productId = "MLA123456789";
        MeliException exception = new MeliException(HttpStatus.INTERNAL_SERVER_ERROR, null, "001003");
        when(productDetailService.getProductDetailByProductId(productId)).thenThrow(exception);

        // When & Then
        mockMvc.perform(get("/detail/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("001003")));

        verify(productDetailService, times(1)).getProductDetailByProductId(productId);
    }

    @Test
    void getProductDetailByProductId_InvalidProductId_ShouldHandleGracefully() throws Exception {
        // Given
        String invalidProductId = "";
        
        // When & Then
        mockMvc.perform(get("/detail/{productId}", invalidProductId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound()); // Spring Boot devuelve 404 para path vacío

        // No debería llamar al servicio para ID vacío
        verify(productDetailService, never()).getProductDetailByProductId(anyString());
    }

    @Test
    void testTracingIntegration_ShouldCreateAndCloseSpans() throws Exception {
        // Given
        when(productDetailService.getAllProductDetails()).thenReturn(sampleProductList);

        // When
        mockMvc.perform(get("/detail"));

        // Then - Verify tracing interactions
        verify(tracer, times(1)).nextSpan();
        verify(span, times(1)).name("ProductDetailControllers.getAllProductDetails");
        verify(span, times(1)).start();
        verify(span, times(1)).end();
    }

    @Test
    void testMetricsAnnotation_ShouldHaveTimedAnnotation() throws Exception {
        // Verify that the controller methods have @Timed annotations
        // This is validated through reflection or by checking if the annotation is present
        var method = ProductDetailControllers.class.getMethod("getAllProductDetails");
        var timedAnnotation = method.getAnnotation(io.micrometer.core.annotation.Timed.class);
        
        // Assert that @Timed annotation exists and has correct value
        assert timedAnnotation != null;
        assert timedAnnotation.value().equals("product_detail.getAllProductDetails");
    }

    @Test
    void getProductDetailByProductId_WithSpecialCharacters_ShouldHandleCorrectly() throws Exception {
        // Given
        String productIdWithSpecialChars = "MLA123-456_789";
        when(productDetailService.getProductDetailByProductId(productIdWithSpecialChars))
                .thenReturn(sampleProductDetail);

        // When & Then
        mockMvc.perform(get("/detail/{productId}", productIdWithSpecialChars)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(productDetailService, times(1)).getProductDetailByProductId(productIdWithSpecialChars);
    }
}