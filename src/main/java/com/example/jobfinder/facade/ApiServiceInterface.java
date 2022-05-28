package com.example.jobfinder.facade;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ApiServiceInterface {
    List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException;

    String getName();

    String getUrl();
}
