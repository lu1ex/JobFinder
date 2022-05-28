package com.example.jobfinder.facade.justjoinit;

import com.example.jobfinder.facade.noFluffJobs.NoFluffJobsModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Postings {
    private List<JustJoinItModel> postings;

}
