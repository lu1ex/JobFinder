package com.example.jobfinder.services;

import com.example.jobfinder.entity.ContractDetailsEntity;
import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.exceptions.ValueNotFoundException;
import com.example.jobfinder.repository.UnifedOfferRepository;
import com.example.jobfinder.requestBodyModels.UnifiedOfferRequestBodyModel;
import com.example.jobfinder.servicesForDownloaders.JobFinderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UnifiedOfferService {
    private final MapperService mapperService;
    private final JobFinderService jobFinderService;
    private final UnifedOfferRepository unifedOfferRepository;

    public UnifiedOfferEntity createUnifiedOfferEntity(UnifiedOfferRequestBodyModel unifiedOfferRequestBodyModel) {
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

    public UnifiedOfferEntity getUnifiedOfferEntityById(String id) {
        return unifedOfferRepository.findById(id).orElseThrow((() -> new ValueNotFoundException(id)));
    }


    public List<UnifiedOfferEntity> getOffersFromServices() throws ExecutionException, InterruptedException {
        return jobFinderService.getAllOffers();
    }

    public void deleteExpiredOffersFromDB(List<UnifiedOfferEntity> unifiedOfferEntityList) {
        List<UnifiedOfferEntity> offersToDelete = new ArrayList<>();
        for (UnifiedOfferEntity unifiedOfferEntity : unifiedOfferEntityList) {
            if (unifiedOfferEntity.getExpirationDate().isAfter(LocalDateTime.now())) {
                offersToDelete.add(unifiedOfferEntity);
            }
        }
        unifedOfferRepository.deleteAll(offersToDelete);
    }

    public void addNewOffersToDB() throws ExecutionException, InterruptedException {
        List<UnifiedOfferEntity> listOfOffersToAdd = new ArrayList<>();
        for (UnifiedOfferEntity offer : getOffersFromServices()) {
            if (!unifedOfferRepository.existsByCityAndStreetAndTitleAndAndCompanyNameAndRemoteAndUrl(
                    offer.getCity(), offer.getStreet(), offer.getTitle(), offer.getCompanyName(),
                    offer.isRemote(), offer.getUrl())) {
                listOfOffersToAdd.add(offer);
            }
        }
        unifedOfferRepository.saveAllAndFlush(listOfOffersToAdd);
    }

}
