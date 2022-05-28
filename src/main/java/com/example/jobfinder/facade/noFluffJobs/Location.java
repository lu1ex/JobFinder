package com.example.jobfinder.facade.noFluffJobs;

import lombok.Data;

import java.util.List;

@Data
public class Location {
    public List<Place> places;
    public boolean fullyRemote;
    public boolean covidTimeRemotely;
}
