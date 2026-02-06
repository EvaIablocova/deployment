package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.GroupDTO;
import com.example.database_microservice.DTOs.GroupMemberDTO;
import com.example.database_microservice.DTOs.JoinGroupRequest;
import com.example.database_microservice.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/db/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // --- CRUD ---

    @GetMapping
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroupById(@PathVariable Long id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupDTO groupDTO) {
        try {
            GroupDTO created = groupService.createGroup(groupDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable Long id, @RequestBody GroupDTO updatedGroupDTO) {
        return groupService.updateGroup(id, updatedGroupDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        return groupService.deleteGroup(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // --- Member Management ---

    @GetMapping("/{id}/members")
    public ResponseEntity<List<GroupMemberDTO>> getGroupMembers(@PathVariable Long id) {
        List<GroupMemberDTO> members = groupService.getGroupMembers(id);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGroup(@RequestBody JoinGroupRequest request) {
        return groupService.joinGroupByInviteCode(request.getInviteCode(), request.getUserId())
                .<ResponseEntity<?>>map(group -> ResponseEntity.ok(group))
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid invite code or user already in a group")));
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<?> addMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestParam Long requesterId) {
        try {
            return groupService.addMemberToGroup(groupId, userId, requesterId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupService.removeMemberFromGroup(groupId, userId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // --- Invite Code ---

    @PostMapping("/{id}/regenerate-invite")
    public ResponseEntity<?> regenerateInviteCode(@PathVariable Long id) {
        return groupService.regenerateInviteCode(id)
                .map(code -> ResponseEntity.ok(Map.of("inviteCode", code)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<GroupDTO> getGroupByInviteCode(@PathVariable String inviteCode) {
        return groupService.getGroupByInviteCode(inviteCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
