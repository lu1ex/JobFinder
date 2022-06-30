package com.example.jobfinder.servicesForDownloaders;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.facade.noFluffJobs.NoFluffJobsModel;
import com.example.jobfinder.facade.noFluffJobs.Postings;
import com.example.jobfinder.repository.UnifedOfferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoFluffyJobsDownloaderService implements ApiServiceInterface {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final UnifedOfferRepository unifedOfferRepository;

    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        Postings postings = objectMapper.readValue(json, Postings.class);
        List<UnifiedOfferEntity> unifiedOfferEntities = new ArrayList<>();
        int counter = 1;
        List<NoFluffJobsModel> filteredOffers = postings.getPostings().stream()
                .filter(noFluffJobsModel -> !unifedOfferRepository.findUrls("nofluffjobs.com")
                        .contains("https://nofluffjobs.com/pl/job/" + noFluffJobsModel.getUrl()))
                .collect(Collectors.toList());

        for (NoFluffJobsModel noFluffJobsModel : filteredOffers) {
            unifiedOfferEntities.add(modelMapper.map(noFluffJobsModel, UnifiedOfferEntity.class));
            counter++;
            if (counter % 300 == 0 || counter == filteredOffers.size()) {
                unifedOfferRepository.saveAllAndFlush(unifiedOfferEntities);
                unifiedOfferEntities.clear();
            }
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "noFluffyJobs";
    }

    @Override
    public String getUrl() {
        return "https://nofluffjobs.com/api/posting";
    }
}
