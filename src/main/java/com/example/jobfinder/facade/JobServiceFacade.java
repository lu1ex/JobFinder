package com.example.jobfinder.facade;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JobServiceFacade {
    private final Map<String, ApiServiceInterface> jobServices;

    public JobServiceFacade(Set<ApiServiceInterface> allJobServices) {
        this.jobServices = allJobServices.stream()
                .collect(Collectors.toMap(ApiServiceInterface::getName, Function.identity()));
    }

    public Set<String> getAllServicesNames() {
        return jobServices.keySet();
    }

    public List<ApiServiceInterface> getJobDownloaders() {
        return new ArrayList<>(jobServices.values());
    }
}
