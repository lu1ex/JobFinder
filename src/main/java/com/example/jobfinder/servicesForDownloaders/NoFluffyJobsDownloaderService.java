package com.example.jobfinder.servicesForDownloaders;

import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.facade.noFluffJobs.Postings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NoFluffyJobsDownloaderService implements ApiServiceInterface {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        Postings postings = objectMapper.readValue(json, Postings.class);

        return Optional.ofNullable(postings.getPostings()).map(noFluffJobsModels -> noFluffJobsModels.stream()
                .map(o -> modelMapper.map(o, UnifiedOfferEntity.class))
                .collect(Collectors.toList())).orElse(new ArrayList<>());
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

