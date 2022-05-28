package com.example.jobfinder.servicesForDownloaders;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.facade.JobServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class JobFinderService {
    private final JobServiceFacade jobServiceFacade;
    private final ExecutorService executorService;
    private final RestTemplate restTemplate;

    public List<UnifiedOfferEntity> getAllOffers() throws ExecutionException, InterruptedException {
        List<UnifiedOfferEntity> allOffers = new ArrayList<>();
        List<Future<List<UnifiedOfferEntity>>> futures = new ArrayList<>();

        for (ApiServiceInterface jobDownloader : jobServiceFacade.getJobDownloaders()) {
            futures.add(executorService.submit(new JobOfferTask(jobDownloader, restTemplate)));
        }

        for (Future<List<UnifiedOfferEntity>> future : futures) {
            allOffers.addAll(future.get());
        }

        return allOffers;
    }

    @RequiredArgsConstructor
    private class JobOfferTask implements Callable<List<UnifiedOfferEntity>> {
        private final ApiServiceInterface apiServiceInterface;
        private final RestTemplate restTemplate;

        @Override
        public List<UnifiedOfferEntity> call() throws Exception {
            return apiServiceInterface.getOffers(restTemplate.getForEntity(apiServiceInterface.getUrl(), String.class).getBody());
        }
    }
}
