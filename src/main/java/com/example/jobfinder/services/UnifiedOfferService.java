package com.example.jobfinder.services;

import com.example.jobfinder.entity.ContractDetailsEntity;
import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.exceptions.ValueNotFoundException;
import com.example.jobfinder.repository.UnifedOfferRepository;
import com.example.jobfinder.requestBodyModels.UnifiedOfferRequestBodyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UnifiedOfferService {
    private final MapperService mapperService;
    private final UnifedOfferRepository unifedOfferRepository;

    public UnifiedOfferEntity createEntity(UnifiedOfferRequestBodyModel unifiedOfferRequestBodyModel) {
        Set<SkillEntity> skills = mapperService.mapSetToSetOfClass(
                unifiedOfferRequestBodyModel.getSkills(), SkillEntity.class);
        Set<ContractDetailsEntity> contractDetails = mapperService.mapSetToSetOfClass(
                unifiedOfferRequestBodyModel.getContractDetails(), ContractDetailsEntity.class);
        Set<SeniorityEntity> seniority = mapperService.mapSetToSetOfClass(
                unifiedOfferRequestBodyModel.getSeniority(), SeniorityEntity.class);
        UnifiedOfferEntity newEntity = new UnifiedOfferEntity(unifiedOfferRequestBodyModel, skills, contractDetails, seniority);
        unifedOfferRepository.save(newEntity);
        return newEntity;
    }

    public UnifiedOfferEntity getEntityById(String id) {
        return unifedOfferRepository.findById(id).orElseThrow((() -> new ValueNotFoundException(id)));
    }

    public Set<UnifiedOfferEntity> getAllEntities() {
        return new HashSet<>(unifedOfferRepository.findAll());
    }

    public void deleteEntityById(String id) {
        unifedOfferRepository.deleteById(id);
    }

    public UnifiedOfferEntity putEntity(String id, UnifiedOfferRequestBodyModel unifiedOfferRequestBodyModel) {
        UnifiedOfferEntity unifiedOfferEntity = getEntityById(id);
        BeanUtils.copyProperties(unifiedOfferRequestBodyModel, unifiedOfferEntity);
        unifiedOfferEntity.setSkills(
                mapperService.mapSetToSetOfClass(unifiedOfferRequestBodyModel.getSkills(), SkillEntity.class));
        unifiedOfferEntity.setSeniority(
                mapperService.mapSetToSetOfClass(unifiedOfferRequestBodyModel.getSeniority(), SeniorityEntity.class));
        unifiedOfferEntity.setContractDetails(
                mapperService.mapSetToSetOfClass(unifiedOfferRequestBodyModel.getContractDetails(), ContractDetailsEntity.class));
        unifedOfferRepository.save(unifiedOfferEntity);
        return unifiedOfferEntity;
    }

    public UnifiedOfferEntity patchEntity(String id, Map<Object, Object> fields) {
        UnifiedOfferEntity unifiedOfferEntity = getEntityById(id);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UnifiedOfferEntity.class, (String) key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, unifiedOfferEntity, value);
        });
        return unifiedOfferEntity;
    }
}
