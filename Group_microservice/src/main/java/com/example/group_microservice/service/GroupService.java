package com.example.group_microservice.service;

import com.example.group_microservice.DTOs.GroupDTO;
import com.example.group_microservice.DTOs.GroupMemberDTO;
import com.example.group_microservice.DTOs.JoinGroupRequest;
import com.example.group_microservice.repository.GroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<GroupDTO> getAllGroups() {
        return groupRepository.getAllGroups();
    }

    public Optional<GroupDTO> getGroupById(Long id) {
        ResponseEntity<GroupDTO> response = groupRepository.getGroupById(id);
        return Optional.ofNullable(response.getBody());
    }

    public ResponseEntity<?> createGroup(GroupDTO groupDTO) {
        return groupRepository.createGroup(groupDTO);
    }

    public ResponseEntity<GroupDTO> updateGroup(Long id, GroupDTO groupDTO) {
        return groupRepository.updateGroup(id, groupDTO);
    }

    public ResponseEntity<Void> deleteGroup(Long id) {
        return groupRepository.deleteGroup(id);
    }

    public List<GroupMemberDTO> getGroupMembers(Long groupId) {
        return groupRepository.getGroupMembers(groupId);
    }

    public ResponseEntity<?> joinGroup(JoinGroupRequest request) {
        return groupRepository.joinGroup(request);
    }

    public ResponseEntity<?> addMember(Long groupId, Long userId, Long requesterId) {
        return groupRepository.addMember(groupId, userId, requesterId);
    }

    public ResponseEntity<Void> removeMember(Long groupId, Long userId) {
        return groupRepository.removeMember(groupId, userId);
    }

    public ResponseEntity<?> regenerateInviteCode(Long groupId) {
        return groupRepository.regenerateInviteCode(groupId);
    }

    public Optional<GroupDTO> getGroupByInviteCode(String inviteCode) {
        ResponseEntity<GroupDTO> response = groupRepository.getGroupByInviteCode(inviteCode);
        return Optional.ofNullable(response.getBody());
    }
}
