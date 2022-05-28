package com.example.jobfinder.facade.noFluffJobs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

/*
POPRAWIC DATE WYGASNIECIA
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoFluffJobsModel {
    private String id;
    private String name;//
    private Location location;//
    private long posted;//
    private long renewed;//odnawia sie co 7 dni
    private String title;//
    private String technology;//
    private List<String> seniority;//
    private String url;//
    private boolean fullyRemote;//
    private Salary salary;//
    private boolean onlineInterviewAvailable;//
}
