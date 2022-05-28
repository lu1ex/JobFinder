package com.example.jobfinder.repository;

import com.example.jobfinder.entity.ContractDetailsEntity;
import com.example.jobfinder.entity.SeniorityEntity;
import com.example.jobfinder.entity.SkillEntity;
import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.enums.Seniority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UnifedOfferRepository extends JpaRepository<UnifiedOfferEntity, String> {

    boolean existsByCityAndStreetAndTitleAndAndCompanyNameAndRemoteAndUrl(
            String city, String street, String title, String companyName, boolean remote, String url);
}
