package com.example.jobfinder.requestBodyModels;

import com.example.jobfinder.modelDTO.UnifiedOfferDTO;
import lombok.Data;
import java.util.Set;

@Data
public class SkillRequestBodyModel {
    private String name;
    private Set<UnifiedOfferDTO> unifiedOffers;
}
