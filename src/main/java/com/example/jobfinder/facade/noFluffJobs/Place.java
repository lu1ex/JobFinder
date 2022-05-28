package com.example.jobfinder.facade.noFluffJobs;

import lombok.Data;

@Data
public class Place {
    public String city;
    public String url;
    public Country country;
    public String street;
    public String postalCode;
    public GeoLocation geoLocation;
}
