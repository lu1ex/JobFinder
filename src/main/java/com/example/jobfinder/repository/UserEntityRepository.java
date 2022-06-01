package com.example.jobfinder.repository;

import com.example.jobfinder.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
}
