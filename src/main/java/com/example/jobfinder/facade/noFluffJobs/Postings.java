package com.example.jobfinder.facade.noFluffJobs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Postings {
    private List<NoFluffJobsModel> postings;

}
