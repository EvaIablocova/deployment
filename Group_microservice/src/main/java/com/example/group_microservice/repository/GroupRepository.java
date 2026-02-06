package com.example.group_microservice.repository;

import com.example.group_microservice.DTOs.GroupDTO;
import com.example.group_microservice.DTOs.GroupMemberDTO;
import com.example.group_microservice.DTOs.JoinGroupRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GroupRepository {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public GroupRepository(RestTemplateBuilder builder, @Value("${database.service.url}") String dbServiceUrl) {
        this.restTemplate = builder.build();
        this.externalBase = dbServiceUrl + "/db/groups";
    }

    public List<GroupDTO> getAllGroups() {
        try {
            ResponseEntity<GroupDTO[]> response = restTemplate.getForEntity(externalBase, GroupDTO[].class);
            GroupDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new GroupDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<GroupDTO> getGroupById(Long id) {
        try {
            ResponseEntity<GroupDTO> response = restTemplate.getForEntity(externalBase + "/" + id, GroupDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> createGroup(GroupDTO groupDTO) {
        try {
            ResponseEntity<GroupDTO> response = restTemplate.postForEntity(externalBase, groupDTO, GroupDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to create group"));
        }
    }

    public ResponseEntity<GroupDTO> updateGroup(Long id, GroupDTO groupDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, groupDTO);
            return ResponseEntity.ok(groupDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteGroup(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<GroupMemberDTO> getGroupMembers(Long groupId) {
        try {
            ResponseEntity<GroupMemberDTO[]> response =
                restTemplate.getForEntity(externalBase + "/" + groupId + "/members", GroupMemberDTO[].class);
            GroupMemberDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new GroupMemberDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<?> joinGroup(JoinGroupRequest request) {
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

    public ResponseEntity<?> addMember(Long groupId, Long userId, Long requesterId) {
        try {
            String url = externalBase + "/" + groupId + "/members/" + userId + "?requesterId=" + requesterId;
            ResponseEntity<GroupDTO> response = restTemplate.postForEntity(url, null, GroupDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add member"));
        }
    }

    public ResponseEntity<Void> removeMember(Long groupId, Long userId) {
        try {
            restTemplate.delete(externalBase + "/" + groupId + "/members/" + userId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> regenerateInviteCode(Long groupId) {
        try {
            ResponseEntity<Map> response =
                restTemplate.postForEntity(externalBase + "/" + groupId + "/regenerate-invite", null, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<GroupDTO> getGroupByInviteCode(String inviteCode) {
        try {
            ResponseEntity<GroupDTO> response =
                restTemplate.getForEntity(externalBase + "/invite/" + inviteCode, GroupDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
