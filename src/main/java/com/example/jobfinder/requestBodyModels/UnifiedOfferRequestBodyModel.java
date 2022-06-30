package com.example.jobfinder.requestBodyModels;

import com.example.jobfinder.modelDTO.ContractDetailsDTO;
import com.example.jobfinder.modelDTO.SeniorityDTO;
import com.example.jobfinder.modelDTO.SkillDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UnifiedOfferRequestBodyModel {
    private String companyName;
    private String city;
    private String street;
    private String title;
    private LocalDateTime expirationDate;
    private String url;
    private boolean remote;
    private boolean remoteRecruitment;
    @NotNull
    private Set<SkillDTO> skills;
    private Set<ContractDetailsDTO> contractDetails;
    private Set<SeniorityDTO> seniority;
}
