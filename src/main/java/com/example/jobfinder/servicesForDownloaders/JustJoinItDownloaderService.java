package com.example.jobfinder.servicesForDownloaders;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.facade.justjoinit.JustJoinItModel;
import com.example.jobfinder.repository.UnifedOfferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JustJoinItDownloaderService implements ApiServiceInterface {
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final UnifedOfferRepository unifedOfferRepository;

    @Override
    public List<UnifiedOfferEntity> getOffers(String json) throws JsonProcessingException {
        JustJoinItModel[] postings = objectMapper.readValue(json, JustJoinItModel[].class);
        List<UnifiedOfferEntity> unifiedOfferEntities = new ArrayList<>();
        int counter = 1;
        List<JustJoinItModel> filteredOffers = Optional.ofNullable(postings).map(justJoinItModels -> Arrays.stream(justJoinItModels)
                .filter(justJoinItModel -> !unifedOfferRepository.findUrls("justjoin.it")
                        .contains("https://justjoin.it/offers/" + justJoinItModel.getId()))
                .collect(Collectors.toList())).get();

        for (JustJoinItModel justJoinItModel : filteredOffers) {
            unifiedOfferEntities.add(modelMapper.map(justJoinItModel, UnifiedOfferEntity.class));
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
        return "justjoinit";
    }

    @Override
    public String getUrl() {
        return "https://justjoin.it/api/offers";
    }
}
