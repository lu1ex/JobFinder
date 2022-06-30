package com.example.jobfinder.services;

import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.repository.SeniorityEntityRepository;
import com.example.jobfinder.repository.SkillEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeniorityService {
    private final SeniorityEntityRepository seniorityEntityRepository;

    public Set<SeniorityEntity> getAll() {
        return new HashSet<>(seniorityEntityRepository.findAll());
    }
}
