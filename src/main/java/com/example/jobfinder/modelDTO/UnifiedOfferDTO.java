package com.example.jobfinder.modelDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UnifiedOfferDTO {
    private String companyName;
    private String city;
    private String street;
    private String title;
    private LocalDateTime expirationDate;
    private String url;
    private boolean remote;
    private boolean remoteRecruitment;
    private Set<SkillDTO> skills;
    private Set<ContractDetailsDTO> contractDetails;
    private Set<SeniorityDTO> seniority;
}
