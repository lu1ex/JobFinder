package com.example.jobfinder.services;

import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.exceptions.ValueNotFoundException;
import com.example.jobfinder.repository.SkillEntityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillEntityService {
    private final SkillEntityRepository skillEntityRepository;
    private final ModelMapper modelMapper;

    public List<SkillEntity> getAllSkillEntities() {
        return skillEntityRepository.findAll();
    }

    public SkillEntity getSkillEntityByID(String id) {
        return skillEntityRepository.findById(id).orElseThrow((() -> new ValueNotFoundException(id)));
    }

    public void deleteSkillEntityByID(String id) {
        SkillEntity skillEntity = getSkillEntityByID(id);
        skillEntityRepository.delete(skillEntity);
    }

  /*  public SkillEntity createSkillEntity(SkillRequestBodyModel skillRequestBodyModel) {
        SkillEntity skillEntity = new SkillEntity(skillRequestBodyModel);
        skillEntityRepository.save(skillEntity);
        return skillEntity;
    }

    public SkillEntity updateSkillEntityByPut(String id, SkillRequestBodyModel skillRequestBodyModel) {
        SkillEntity skillEntity = getSkillEntityByID(id);
       *//* BeanUtils.copyProperties(skillRequestBodyModel, skillEntity);
        BeanUtils.copyProperties(skillRequestBodyModel.getUnifiedOffers(), skillEntity.getUnifiedOffers());*//*
        //skillEntity.setUnifiedOffers(skillRequestBodyModel.getUnifiedOffers());

        return skillEntity;
    }

    public SkillEntity updateSkillEntityByPatch(String id, SkillRequestBodyModel skillRequestBodyModel) {
        SkillEntity skillEntity = getSkillEntityByID(id);
        return null;

    }*/
}
