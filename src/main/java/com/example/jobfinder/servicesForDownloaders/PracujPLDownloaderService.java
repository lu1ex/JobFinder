package com.example.jobfinder.servicesForDownloaders;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.facade.pracujPL.Offers;
import com.example.jobfinder.facade.pracujPL.PracujPLModel;
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
public class PracujPLDownloaderService implements ApiServiceInterface {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final UnifedOfferRepository unifedOfferRepository;

    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        List<UnifiedOfferEntity> result = objectMapper.readValue(json, Offers.class).getOffers().stream()
                        .filter(pracujPLModel -> !unifedOfferRepository.findUrls("pracuj.pl")
                                .contains(pracujPLModel.getOfferUrl()))
                                .collect(Collectors.toList()).stream()
                .map(pracujPLModel -> modelMapper.map(pracujPLModel, UnifiedOfferEntity.class))
                .collect(Collectors.toList());

        unifedOfferRepository.saveAllAndFlush(result);
        return new ArrayList<>();
    }


/* @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        Offers postings = objectMapper.readValue(json, Offers.class);
        System.out.println("oferty z api: " + postings.getOffers().size());
        List<String> urlsInDataBase = unifedOfferRepository.findUrls("pracuj.pl");
        System.out.println("urls z db: " + urlsInDataBase.size());
        List<PracujPLModel> filteredOffers = postings.getOffers().stream()
                .filter(pracujPLModel -> !urlsInDataBase.contains(pracujPLModel.getOfferUrl()))
                .collect(Collectors.toList());
        System.out.println("po filtrowaniu: " + filteredOffers.size());
        return filteredOffers.stream()
                .map(pracujPLModel -> modelMapper.map(pracujPLModel, UnifiedOfferEntity.class))
                .collect(Collectors.toList());
    }*//*

*/
/*
    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        Offers postings = objectMapper.readValue(json, Offers.class);

        return Optional.ofNullable(postings.getOffers()).map(pracujPLModels -> pracujPLModels.stream()
                .map(o -> modelMapper.map(o, UnifiedOfferEntity.class))
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }*/



    @Override
    public String getName() {
        return "pracujPL";
    }

    @Override
    public String getUrl() {
        return "https://massachusetts.pracuj.pl/api/offers";
    }
}

