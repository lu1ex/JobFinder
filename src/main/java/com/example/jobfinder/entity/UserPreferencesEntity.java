package com.example.jobfinder.entity;

import com.example.jobfinder.forms.UserPreferencesForm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "preferences")
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferencesEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;
    private String city;
    private boolean remote = false;
    private boolean remoteRecruitment = false;
    private double salaryMin;
    private boolean uop;
    private boolean b2b;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "owned_experience",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "seniority_id"))
    private Set<SeniorityEntity> ownedSeniority;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "owned_skills",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<SkillEntity> ownedSkills;
    @OneToOne(mappedBy = "userPreferences")
    private UserEntity userEntity;

    public UserPreferencesEntity(UserPreferencesForm userPreferencesForm, Set<SkillEntity> ownedSkills,
                                 Set<SeniorityEntity> ownedSeniority, UserEntity userEntity) {
        this.id = UUID.randomUUID().toString();
        this.city = userPreferencesForm.getCity();
        this.remote = userPreferencesForm.isRemote();
        this.remoteRecruitment = userPreferencesForm.isRemoteRecruitment();
        this.salaryMin = userPreferencesForm.getSalaryMin();
        this.uop = userPreferencesForm.isUop();
        this.b2b = userPreferencesForm.isB2b();
        this.ownedSeniority = ownedSeniority;
        this.ownedSkills = ownedSkills;
        this.userEntity = userEntity;
    }
}
