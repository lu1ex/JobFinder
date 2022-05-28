package com.example.jobfinder.facade.justjoinit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JustJoinItModel {
    public String title;//
    public String street;//
    public String city;//
    @JsonProperty(value = "company_name")
    public String companyName;//
    @JsonProperty(value = "experience_level")
    public String experienceLevel;//
    @JsonProperty(value = "published_at")
    public Date publishedAt;//
    public boolean remoteInterview;//
    @JsonProperty(value = "employment_types")
    public List<EmploymentType> employmentTypes;//
    public List<Skill> skills;//
    public boolean remote;//
    public String id;
}
