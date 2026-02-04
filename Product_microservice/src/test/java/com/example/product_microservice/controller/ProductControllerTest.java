package com.example.product_microservice.controller;

import com.example.product_microservice.DTOs.ProductDTO;
import com.example.product_microservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Test
    void getAllProducts_returnsList() {
        ProductDTO dto1 = mock(ProductDTO.class);
        ProductDTO dto2 = mock(ProductDTO.class);
        List<ProductDTO> expected = Arrays.asList(dto1, dto2);

        when(productService.getAllProducts()).thenReturn(expected);

        List<ProductDTO> result = productController.getAllProducts();

        assertNotNull(result);
        assertEquals(expected, result);
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_found_returnsOkWithBody() {
        Long id = 1L;
        ProductDTO dto = mock(ProductDTO.class);

        when(productService.getProductById(id)).thenReturn(Optional.of(dto));

        ResponseEntity<ProductDTO> response = productController.getProductById(id);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
        verify(productService, times(1)).getProductById(id);
    }

    @Test
    void getProductById_notFound_returnsNotFound() {
        Long id = 2L;

        when(productService.getProductById(id)).thenReturn(Optional.empty());

        ResponseEntity<ProductDTO> response = productController.getProductById(id);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(productService, times(1)).getProductById(id);
    }

    @Test
    void createProduct_returnsCreatedDto() {
        ProductDTO input = mock(ProductDTO.class);
        ProductDTO created = mock(ProductDTO.class);

        when(productService.createProduct(input)).thenReturn(created);

        ProductDTO result = productController.createProduct(input);

        assertNotNull(result);
        assertEquals(created, result);
        verify(productService, times(1)).createProduct(input);
    }

    @Test
    void updateProduct_delegatesToService_andReturnsServiceResponse() {
        Long id = 3L;
        ProductDTO updated = mock(ProductDTO.class);
        ResponseEntity<ProductDTO> serviceResponse = ResponseEntity.ok(updated);

        when(productService.updateProduct(id, updated)).thenReturn(serviceResponse);

        ResponseEntity<ProductDTO> response = productController.updateProduct(id, updated);

        assertNotNull(response);
        assertEquals(serviceResponse, response);
        verify(productService, times(1)).updateProduct(id, updated);
    }

    @Test
    void deleteProduct_delegatesToService_andReturnsServiceResponse() {
        Long id = 4L;
        ResponseEntity<Void> serviceResponse = ResponseEntity.noContent().build();

        when(productService.deleteProduct(id)).thenReturn(serviceResponse);

        ResponseEntity<Void> response = productController.deleteProduct(id);

        assertNotNull(response);
        assertEquals(serviceResponse, response);
        verify(productService, times(1)).deleteProduct(id);
    }
}
