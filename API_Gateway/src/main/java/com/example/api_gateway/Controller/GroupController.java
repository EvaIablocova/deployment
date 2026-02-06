package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.AddMemberRequest;
import com.example.api_gateway.DTOs.GroupDTO;
import com.example.api_gateway.DTOs.GroupMemberDTO;
import com.example.api_gateway.DTOs.JoinGroupRequest;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/groups")
public class GroupController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public GroupController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getGroupServiceUrl() + "/api/groups";
    }

    // --- CRUD ---

    @GetMapping
    public List<GroupDTO> getAllGroups() {
        try {
            ResponseEntity<GroupDTO[]> response = restTemplate.getForEntity(externalBase, GroupDTO[].class);
            GroupDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new GroupDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable Long id) {
        try {
            ResponseEntity<GroupDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, GroupDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            ResponseEntity<GroupDTO> response =
                    restTemplate.postForEntity(externalBase, groupDTO, GroupDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to create group"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable Long id, @RequestBody GroupDTO updatedGroupDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedGroupDTO);
            return ResponseEntity.ok(updatedGroupDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Member Management ---

    @GetMapping("/{id}/members")
    public ResponseEntity<List<GroupMemberDTO>> getGroupMembers(@PathVariable Long id) {
        try {
            ResponseEntity<GroupMemberDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/" + id + "/members", GroupMemberDTO[].class);
            GroupMemberDTO[] body = response.getBody();
            return ResponseEntity.ok(Arrays.asList(Optional.ofNullable(body).orElse(new GroupMemberDTO[0])));
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGroup(@RequestBody JoinGroupRequest request) {
        try {
            ResponseEntity<GroupDTO> response =
                    restTemplate.postForEntity(externalBase + "/join", request, GroupDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to join group"));
        }
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<?> addMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestBody AddMemberRequest request) {
        try {
            ResponseEntity<GroupDTO> response =
                    restTemplate.postForEntity(
                            externalBase + "/" + groupId + "/members/" + userId,
                            request,
                            GroupDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add member"));
        }
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        try {
            restTemplate.delete(externalBase + "/" + groupId + "/members/" + userId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Invite Code ---

    @PostMapping("/{id}/regenerate-invite")
    public ResponseEntity<?> regenerateInviteCode(@PathVariable Long id) {
        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(externalBase + "/" + id + "/regenerate-invite", null, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<GroupDTO> getGroupByInviteCode(@PathVariable String inviteCode) {
        try {
            ResponseEntity<GroupDTO> response =
                    restTemplate.getForEntity(externalBase + "/invite/" + inviteCode, GroupDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
