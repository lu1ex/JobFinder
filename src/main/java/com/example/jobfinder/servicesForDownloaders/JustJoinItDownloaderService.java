package com.example.jobfinder.servicesForDownloaders;

import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.facade.justjoinit.JustJoinItModel;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JustJoinItDownloaderService implements ApiServiceInterface {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        JustJoinItModel[] postings = objectMapper.readValue(json, JustJoinItModel[].class);

        return Optional.ofNullable(postings).map(justJoinItModels -> Arrays.stream(justJoinItModels)
                .filter(Objects::nonNull)
                .map(o -> modelMapper.map(o, UnifiedOfferEntity.class))
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    @Override
    public String getName() {
        return "justjoinit";
    }

    @Override
    public String getUrl() {
        return "https://justjoin.it/api/offers";
    }
}
