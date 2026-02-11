package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.EntityType;
import com.example.api_gateway.DTOs.ImageDTO;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/images")
public class ImageController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public ImageController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getImageServiceUrl() + "/api/images";
    }

    @GetMapping
    public List<ImageDTO> getAllImages() {
        try {
            ResponseEntity<ImageDTO[]> response = restTemplate.getForEntity(externalBase, ImageDTO[].class);
            ImageDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ImageDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id) {
        try {
            ResponseEntity<ImageDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, ImageDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public List<ImageDTO> getImagesByEntity(
            @PathVariable EntityType entityType,
            @PathVariable Long entityId) {
        try {
            ResponseEntity<ImageDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/entity/" + entityType + "/" + entityId, ImageDTO[].class);
            ImageDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ImageDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entityType") EntityType entityType,
            @RequestParam("entityId") Long entityId,
            @RequestParam(value = "uploadedBy", required = false) Long uploadedBy) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file));
            body.add("entityType", entityType.name());
            body.add("entityId", entityId.toString());
            if (uploadedBy != null) {
                body.add("uploadedBy", uploadedBy.toString());
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ImageDTO> response = restTemplate.exchange(
                    externalBase,
                    HttpMethod.POST,
                    requestEntity,
                    ImageDTO.class);

            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException | IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long id) {
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    externalBase + "/" + id + "/download",
                    HttpMethod.GET,
                    null,
                    byte[].class);

            HttpHeaders responseHeaders = new HttpHeaders();
            if (response.getHeaders().getContentType() != null) {
                responseHeaders.setContentType(response.getHeaders().getContentType());
            }
            if (response.getHeaders().getContentDisposition().getFilename() != null) {
                responseHeaders.setContentDisposition(response.getHeaders().getContentDisposition());
            }

            return new ResponseEntity<>(response.getBody(), responseHeaders, HttpStatus.OK);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private static class MultipartInputStreamFileResource extends ByteArrayResource {
        private final String filename;

        MultipartInputStreamFileResource(MultipartFile file) throws IOException {
            super(file.getBytes());
            this.filename = file.getOriginalFilename();
        }

        @Override
        public String getFilename() {
            return this.filename;
        }
    }
}
