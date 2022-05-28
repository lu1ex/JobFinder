package com.example.jobfinder.servicesForDownloaders;

import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.facade.pracujPL.Offers;
import com.example.jobfinder.entity.UnifiedOfferEntity;
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
public class PracujPLDownloaderService implements ApiServiceInterface {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        Offers postings = objectMapper.readValue(json, Offers.class);

        return Optional.ofNullable(postings.getOffers()).map(pracujPLModels -> pracujPLModels.stream()
                .map(o -> modelMapper.map(o, UnifiedOfferEntity.class))
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    @Override
    public String getName() {
        return "pracujPL";
    }

    @Override
    public String getUrl() {
        return "https://massachusetts.pracuj.pl/api/offers";
    }
}

