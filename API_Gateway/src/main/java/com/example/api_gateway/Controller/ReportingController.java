package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.ReportDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/reports")
public class ReportingController {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://reportingmicroservice:9017/api/reports/reports/users";

    public ReportingController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @GetMapping
    public List<ReportDTO> getAllReportsByAllUsers() {
        try {
            ResponseEntity<ReportDTO[]> response = restTemplate.getForEntity(externalBase, ReportDTO[].class);
            ReportDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new ReportDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ReportDTO> getReportByUserId(@PathVariable Long id) {
//        try {
//            ResponseEntity<ReportDTO> response =
//                    restTemplate.getForEntity(externalBase + "/" + id, ReportDTO.class);
//            return ResponseEntity.ok(response.getBody());
//        } catch (RestClientException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

}