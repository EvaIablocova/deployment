package com.example.product_microservice.service;

import com.example.product_microservice.DTOs.ProductDTO;
import com.example.product_microservice.repository.ImageRepository;
import com.example.product_microservice.repository.ProductRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_returnsRepositoryResult() {
        ProductDTO dto1 = new ProductDTO();
        dto1.setId(1L);
        ProductDTO dto2 = new ProductDTO();
        dto2.setId(2L);
        List<ProductDTO> expected = Arrays.asList(dto1, dto2);

        when(productRepository.getAllProducts()).thenReturn(expected);
        when(imageRepository.getImagesByProduct(anyLong())).thenReturn(List.of());

        List<ProductDTO> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        verify(productRepository, times(1)).getAllProducts();
    }

    @Test
    void getProductById_whenRepositoryReturnsResponseWithBody_returnsOptionalWithBody() {
        Long id = 1L;
        ProductDTO dto = new ProductDTO();
        dto.setId(id);
        when(productRepository.getProductById(id)).thenReturn(ResponseEntity.ok(dto));
        when(imageRepository.getImagesByProduct(id)).thenReturn(List.of());

        Optional<ProductDTO> result = productService.getProductById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(productRepository, times(1)).getProductById(id);
    }

    @Test
    void getProductById_whenRepositoryReturnsNullResponse_returnsEmptyOptional() {
        Long id = 2L;
        when(productRepository.getProductById(id)).thenReturn(null);

        Optional<ProductDTO> result = productService.getProductById(id);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).getProductById(id);
    }

    @Test
    void getProductById_whenRepositoryReturnsResponseWithNullBody_returnsEmptyOptional() {
        Long id = 3L;
        when(productRepository.getProductById(id)).thenReturn(ResponseEntity.ok(null));

        Optional<ProductDTO> result = productService.getProductById(id);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).getProductById(id);
    }

    @Test
    void createProduct_returnsBodyFromRepositoryResponse() {
        ProductDTO input = new ProductDTO();
        ProductDTO created = new ProductDTO();
        created.setId(1L);

        when(productRepository.createProduct(input)).thenReturn(ResponseEntity.ok(created));
        when(imageRepository.getImagesByProduct(1L)).thenReturn(List.of());

        ProductDTO result = productService.createProduct(input);

        assertNotNull(result);
        assertEquals(created, result);
        verify(productRepository, times(1)).createProduct(input);
    }

    @Test
    void createProduct_whenRepositoryReturnsResponseWithNullBody_returnsNull() {
        ProductDTO input = mock(ProductDTO.class);
        when(productRepository.createProduct(input)).thenReturn(ResponseEntity.ok(null));

        ProductDTO result = productService.createProduct(input);

        assertNull(result);
        verify(productRepository, times(1)).createProduct(input);
    }

    @Test
    void updateProduct_delegatesToRepository_andReturnsResponse() {
        Long id = 4L;
        ProductDTO updated = new ProductDTO();
        updated.setId(id);
        ResponseEntity<ProductDTO> repoResponse = ResponseEntity.ok(updated);

        when(productRepository.updateProduct(id, updated)).thenReturn(repoResponse);
        when(imageRepository.getImagesByProduct(id)).thenReturn(List.of());

        ResponseEntity<ProductDTO> result = productService.updateProduct(id, updated);

        assertEquals(repoResponse.getStatusCode(), result.getStatusCode());
        verify(productRepository, times(1)).updateProduct(id, updated);
    }

    @Test
    void deleteProduct_delegatesToRepository_andReturnsResponse() {
        Long id = 5L;
        ResponseEntity<Void> repoResponse = ResponseEntity.noContent().build();

        when(imageRepository.getImagesByProduct(id)).thenReturn(List.of());
        when(productRepository.deleteProduct(id)).thenReturn(repoResponse);

        ResponseEntity<Void> result = productService.deleteProduct(id);

        assertEquals(repoResponse, result);
        verify(productRepository, times(1)).deleteProduct(id);
    }
}
