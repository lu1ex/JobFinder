package com.example.jobfinder.forms;

import com.example.jobfinder.modelDTO.SkillDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesForm {
    @NotNull
    private String city;
    private boolean remote = false;
    private boolean remoteRecruitment = false;
    private boolean uop = false;
    private boolean b2b = true;
    @NotNull
    private double salaryMin;
    @NotNull
    private Set<String> requiredTypeOfContract;
    @NotNull
    private Set<String> ownedSeniority;
    @NotNull
    private Set<SkillDTO> ownedSkills;
}
