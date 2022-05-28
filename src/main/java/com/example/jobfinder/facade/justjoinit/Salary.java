package com.example.jobfinder.facade.justjoinit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Salary {
    public int from;
    @JsonProperty("to")
    public int myto;
    public String currency;
}
