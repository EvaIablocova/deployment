package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.UserGroup;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GroupDTO {

    private Long id;
    private String name;
    private String inviteCode;
    private LocalDateTime createdAt;
    private Long createdBy;
    private List<GroupMemberDTO> members;

    public GroupDTO() { }

    public GroupDTO(UserGroup group) {
        this.id = group.getId();
        this.name = group.getName();
        this.inviteCode = group.getInviteCode();
        this.createdAt = group.getCreatedAt();
        this.createdBy = group.getCreatedBy();
    }

    public GroupDTO(UserGroup group, boolean includeMembers) {
        this(group);
        if (includeMembers && group.getMembers() != null) {
            this.members = group.getMembers().stream()
                    .map(GroupMemberDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
