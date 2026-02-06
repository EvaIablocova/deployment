package com.example.group_microservice.controller;

import com.example.group_microservice.DTOs.AddMemberRequest;
import com.example.group_microservice.DTOs.GroupDTO;
import com.example.group_microservice.DTOs.GroupMemberDTO;
import com.example.group_microservice.DTOs.JoinGroupRequest;
import com.example.group_microservice.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
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
        return groupService.createGroup(groupDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable Long id, @RequestBody GroupDTO groupDTO) {
        return groupService.updateGroup(id, groupDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        return groupService.deleteGroup(id);
    }

    // --- Member Management ---

    @GetMapping("/{id}/members")
    public ResponseEntity<List<GroupMemberDTO>> getGroupMembers(@PathVariable Long id) {
        List<GroupMemberDTO> members = groupService.getGroupMembers(id);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinGroup(@RequestBody JoinGroupRequest request) {
        return groupService.joinGroup(request);
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<?> addMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestBody AddMemberRequest request) {
        return groupService.addMember(groupId, userId, request.getRequesterId());
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupService.removeMember(groupId, userId);
    }

    // --- Invite Code ---

    @PostMapping("/{id}/regenerate-invite")
    public ResponseEntity<?> regenerateInviteCode(@PathVariable Long id) {
        return groupService.regenerateInviteCode(id);
    }

    @GetMapping("/invite/{inviteCode}")
    public ResponseEntity<GroupDTO> getGroupByInviteCode(@PathVariable String inviteCode) {
        return groupService.getGroupByInviteCode(inviteCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
