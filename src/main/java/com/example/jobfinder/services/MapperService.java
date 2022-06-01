package com.example.jobfinder.services;

import com.example.jobfinder.entity.ContractDetailsEntity;
import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.modelDTO.ContractDetailsDTO;
import com.example.jobfinder.modelDTO.SeniorityDTO;
import com.example.jobfinder.modelDTO.SkillDTO;
import com.example.jobfinder.modelDTO.UnifiedOfferDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MapperService {
    private final ModelMapper mapper;

    public SkillDTO mapSkillEntityToDTO(SkillEntity skillEntity) {
        return mapper.map(skillEntity, SkillDTO.class);
    }

    public <S, T> Set<T> mapSetToSetOfClass(Set<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> mapper.map(element, targetClass))
                .collect(Collectors.toSet());
    }

    public <S, T> T mapObjectToOtherObject(S source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }

   /* public Set<SkillEntity> mapSetOfSkillDTOToSetOfSkillEntity(Set<SkillDTO> skillDTOSet) {
        return skillDTOSet.stream()
                .map(dto -> mapper.map(dto, SkillEntity.class))
                .collect(Collectors.toSet());
    }

    public Set<ContractDetailsEntity> mapSetOfContractDetailslDTOToSetOfConstractDetailsEntity(Set<ContractDetailsDTO> contractDetailsDTOSet) {
        return contractDetailsDTOSet.stream()
                .map(dto -> mapper.map(dto, ContractDetailsEntity.class))
                .collect(Collectors.toSet());
    }

    public Set<SeniorityEntity> mapSetOfSeniorityDTOToSetOfSeniorityEntity(Set<SeniorityDTO> seniorityDTOSet) {
        return seniorityDTOSet.stream()
                .map(dto -> mapper.map(dto, SeniorityEntity.class))
                .collect(Collectors.toSet());
    }*/

    /*
    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        Postings postings = objectMapper.readValue(json, Postings.class);

        return Optional.ofNullable(postings.getPostings()).map(noFluffJobsModels -> noFluffJobsModels.stream()
                .map(o -> modelMapper.map(o, UnifiedOfferEntity.class))
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }
     */

  /*  public SkillEntity mapSkillRequestBodyModelToEntity(SkillRequestBodyModel skillRequestBodyModel) {
        return mapper.map(skillRequestBodyModel, SkillEntity.class);
    }*/
}
