package com.example.jobfinder.repository;

import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.enums.Seniority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeniorityEntityRepository extends JpaRepository<SeniorityEntity, String> {
/*
    Optional<SeniorityEntity> findBySeniority(Seniority seniority);*/

    Optional<SeniorityEntity> findByName(String seniority);

    @Query(value = "SELECT s.name FROM SeniorityEntity s")
    List<String> getAllSeniorityNames();
}
