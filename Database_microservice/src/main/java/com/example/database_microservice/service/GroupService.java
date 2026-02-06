package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.GroupDTO;
import com.example.database_microservice.DTOs.GroupMemberDTO;
import com.example.database_microservice.model.UserGroup;
import com.example.database_microservice.model.User;
import com.example.database_microservice.model.UserRole;
import com.example.database_microservice.repository.GroupRepository;
import com.example.database_microservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private static final String INVITE_CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int INVITE_CODE_LENGTH = 8;
    private final SecureRandom random = new SecureRandom();

    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    private String generateInviteCode() {
        StringBuilder code;
        do {
            code = new StringBuilder();
            for (int i = 0; i < INVITE_CODE_LENGTH; i++) {
                code.append(INVITE_CODE_CHARS.charAt(random.nextInt(INVITE_CODE_CHARS.length())));
            }
        } while (groupRepository.existsByInviteCode(code.toString()));
        return code.toString();
    }

    // --- CRUD ---

    public List<GroupDTO> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(GroupDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<GroupDTO> getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(group -> new GroupDTO(group, true));
    }

    @Transactional
    public GroupDTO createGroup(GroupDTO groupDTO) {
        // Check if the creator is a PREMIUM_USER
        Optional<User> creator = userRepository.findById(groupDTO.getCreatedBy());
        if (creator.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = creator.get();
        if (user.getRole() != UserRole.PREMIUM_USER) {
            throw new IllegalStateException("Only PREMIUM_USER can create groups");
        }

        // Check if user is already in a group
        if (user.getUserGroup() != null) {
            throw new IllegalStateException("User is already in a group");
        }

        UserGroup group = new UserGroup(groupDTO);
        group.setInviteCode(generateInviteCode());

        UserGroup savedGroup = groupRepository.save(group);

        // Add creator to the group
        user.setUserGroup(savedGroup);
        userRepository.save(user);

        return new GroupDTO(savedGroup);
    }

    public Optional<GroupDTO> updateGroup(Long id, GroupDTO updatedGroupDTO) {
        return groupRepository.findById(id).map(group -> {
            group.setName(updatedGroupDTO.getName());
            return new GroupDTO(groupRepository.save(group));
        });
    }

    @Transactional
    public boolean deleteGroup(Long id) {
        if (groupRepository.existsById(id)) {
            // Remove group reference from all members first
            Optional<UserGroup> group = groupRepository.findById(id);
            if (group.isPresent()) {
                for (User member : group.get().getMembers()) {
                    member.setUserGroup(null);
                    userRepository.save(member);
                }
            }
            groupRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Member Management ---

    public List<GroupMemberDTO> getGroupMembers(Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> group.getMembers().stream()
                        .map(user -> new GroupMemberDTO(user, group.getCreatedBy()))
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Transactional
    public Optional<GroupDTO> joinGroupByInviteCode(String inviteCode, Long userId) {
        Optional<UserGroup> groupOpt = groupRepository.findByInviteCode(inviteCode);
        Optional<User> userOpt = userRepository.findById(userId);

        if (groupOpt.isPresent() && userOpt.isPresent()) {
            User user = userOpt.get();

            // Check if user is already in a group
            if (user.getUserGroup() != null) {
                return Optional.empty();
            }

            UserGroup group = groupOpt.get();
            user.setUserGroup(group);
            userRepository.save(user);

            return Optional.of(new GroupDTO(group, true));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<GroupDTO> addMemberToGroup(Long groupId, Long userId, Long requesterId) {
        Optional<UserGroup> groupOpt = groupRepository.findById(groupId);
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<User> requesterOpt = userRepository.findById(requesterId);

        if (groupOpt.isEmpty() || userOpt.isEmpty() || requesterOpt.isEmpty()) {
            return Optional.empty();
        }

        User requester = requesterOpt.get();
        UserGroup group = groupOpt.get();

        // Only PREMIUM_USER who created the group can add participants
        if (requester.getRole() != UserRole.PREMIUM_USER || !group.getCreatedBy().equals(requesterId)) {
            throw new IllegalStateException("Only the group owner (PREMIUM_USER) can add participants");
        }

        User user = userOpt.get();

        // Check if user is already in a group
        if (user.getUserGroup() != null) {
            throw new IllegalStateException("User is already in a group");
        }

        user.setUserGroup(group);
        userRepository.save(user);

        return Optional.of(new GroupDTO(group, true));
    }

    @Transactional
    public boolean removeMemberFromGroup(Long groupId, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getUserGroup() != null && user.getUserGroup().getId().equals(groupId)) {
                user.setUserGroup(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // --- Invite Code Management ---

    public Optional<String> regenerateInviteCode(Long groupId) {
        return groupRepository.findById(groupId).map(group -> {
            String newCode = generateInviteCode();
            group.setInviteCode(newCode);
            groupRepository.save(group);
            return newCode;
        });
    }

    public Optional<GroupDTO> getGroupByInviteCode(String inviteCode) {
        return groupRepository.findByInviteCode(inviteCode)
                .map(GroupDTO::new);
    }
}
