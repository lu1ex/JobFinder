package com.example.jobfinder.repository;

import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.enums.Seniority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeniorityEntityRepository extends JpaRepository<SeniorityEntity, String> {

    Optional<SeniorityEntity> findBySeniority(Seniority seniority);
    Optional<SeniorityEntity> findBySeniority(String seniority);
}
