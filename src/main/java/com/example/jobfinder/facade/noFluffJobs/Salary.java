package com.example.jobfinder.facade.noFluffJobs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Salary {
    public int from;
    @JsonProperty("to")
    public int myto;
    public String type;
    public String currency;
}
