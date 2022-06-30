package com.example.jobfinder.repository;

import com.example.jobfinder.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SkillEntityRepository extends JpaRepository<SkillEntity, String> {

    Optional<SkillEntity> findByName(String name);

    @Query(value = "SELECT s.name FROM SkillEntity s")
    List<String> getAllSkillNames();
}
