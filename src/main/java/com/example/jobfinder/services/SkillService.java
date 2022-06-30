package com.example.jobfinder.services;

import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.repository.SkillEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SkillService {
    private final SkillEntityRepository skillEntityRepository;

    public Set<SkillEntity> getAll() {
        return new HashSet<>(skillEntityRepository.findAll());
    }
}
