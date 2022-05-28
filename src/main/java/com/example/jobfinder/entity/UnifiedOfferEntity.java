package com.example.jobfinder.entity;

import com.example.jobfinder.requestBodyModels.UnifiedOfferRequestBodyModel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "unifiedOffers")
@AllArgsConstructor
@NoArgsConstructor
public class UnifiedOfferEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String uniqueIdentifier;
    private String companyName;
    private String city;
    private String street;
    private String title;
    private LocalDateTime expirationDate;
    private String url;
    private boolean remote;
    private boolean remoteRecruitment;
    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "required_skill",
            joinColumns = @JoinColumn(name = "uniqueIdentifier"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<SkillEntity> skills = new HashSet<>();
    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "terms_of_contract",
            joinColumns = @JoinColumn(name = "uniqueIdentifier"),
            inverseJoinColumns = @JoinColumn(name = "contract_id"))
    private Set<ContractDetailsEntity> contractDetails = new HashSet<>();
    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "required_seniority",
            joinColumns = @JoinColumn(name = "uniqueIdentifier"),
            inverseJoinColumns = @JoinColumn(name = "seniority_id"))
    private Set<SeniorityEntity> seniority = new HashSet<>();

    public UnifiedOfferEntity(UnifiedOfferRequestBodyModel unifiedOfferRequestBodyModel, Set<SkillEntity> skills,
    Set<ContractDetailsEntity> contractDetails, Set<SeniorityEntity> seniority) {
        this.companyName = unifiedOfferRequestBodyModel.getCompanyName();
        this.city = unifiedOfferRequestBodyModel.getCity();
        this.street = unifiedOfferRequestBodyModel.getStreet();
        this.title = unifiedOfferRequestBodyModel.getStreet();
        this.expirationDate = unifiedOfferRequestBodyModel.getExpirationDate();
        this.url = unifiedOfferRequestBodyModel.getUrl();
        this.remote = unifiedOfferRequestBodyModel.isRemote();
        this.remoteRecruitment = unifiedOfferRequestBodyModel.isRemoteRecruitment();
        this.skills = skills;
        this.contractDetails = contractDetails;
        this.seniority = seniority;
    }
}
