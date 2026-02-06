package com.example.database_microservice.repository;

import com.example.database_microservice.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByInviteCode(String inviteCode);
    boolean existsByInviteCode(String inviteCode);
}
