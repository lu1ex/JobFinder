package com.example.jobfinder.repository;

import com.example.jobfinder.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillEntityRepository extends JpaRepository<SkillEntity, String> {

    Optional<SkillEntity> findByName(String name);
}
