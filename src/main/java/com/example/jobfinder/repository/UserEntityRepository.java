package com.example.jobfinder.repository;

import com.example.jobfinder.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, String> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    UserEntity findByLogin(String login);
    Optional<UserEntity> findByEmail(String email);
}
