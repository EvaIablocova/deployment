package com.example.product_microservice.controller;

import com.example.product_microservice.DTOs.ImageDTO;
import com.example.product_microservice.service.ImageService;
import com.example.product_microservice.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {

    private final ImageService imageService;
    private final ProductService productService;

    public ProductImageController(ImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ImageDTO>> getProductImages(@PathVariable Long productId) {
        if (productService.getProductById(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageService.getImagesByProduct(productId));
    }

    @GetMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ImageDTO> getProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        if (productService.getProductById(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return imageService.getImageById(imageId)
                .filter(img -> img.getEntityId().equals(productId))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{productId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploadedBy", required = false) Long uploadedBy) {
        if (productService.getProductById(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return imageService.uploadImage(file, productId, uploadedBy);
    }

    @GetMapping("/{productId}/images/{imageId}/download")
    public ResponseEntity<byte[]> downloadProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        if (productService.getProductById(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return imageService.getImageById(imageId)
                .filter(img -> img.getEntityId().equals(productId))
                .map(img -> imageService.downloadImage(imageId))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Void> deleteProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        if (productService.getProductById(productId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return imageService.getImageById(imageId)
                .filter(img -> img.getEntityId().equals(productId))
                .map(img -> imageService.deleteImage(imageId))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
