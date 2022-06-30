package com.example.jobfinder.services;

import com.example.jobfinder.entity.*;
import com.example.jobfinder.facade.ApiServiceInterface;
import com.example.jobfinder.facade.JobServiceFacade;
import com.example.jobfinder.repository.SeniorityEntityRepository;
import com.example.jobfinder.repository.SkillEntityRepository;
import com.example.jobfinder.repository.UnifedOfferRepository;
import com.example.jobfinder.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class JobFinderService {
    private final UnifedOfferRepository unifedOfferRepository;
    private final SkillEntityRepository skillEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final SeniorityEntityRepository seniorityEntityRepository;
    private final JobServiceFacade jobServiceFacade;
    private final ExecutorService executorService;
    private final RestTemplate restTemplate;
    private final EmailSenderService emailSenderService;

    @Scheduled(cron = "0 0 6 * * *")//(cron second, minute, hour, day of month, month, day(s) of week)
    public void getAllOffers() throws ExecutionException, InterruptedException {
        checkIfEverySkillExistInDB();
        checkIfEverySeniorityExistInDB();

        List<UnifiedOfferEntity> allOffers = new ArrayList<>();
        List<Future<List<UnifiedOfferEntity>>> futures = new ArrayList<>();

        for (ApiServiceInterface jobDownloader : jobServiceFacade.getJobDownloaders()) {
            futures.add(executorService.submit(new JobOfferTask(jobDownloader, restTemplate)));
        }

        for (Future<List<UnifiedOfferEntity>> future : futures) {
            allOffers.addAll(future.get());
        }
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

    @Scheduled(cron = "0 0 23 * * *") //usuń stare oferty, których data już minęła, codziennie o 23
    public void removeExpiredOffers() {
        unifedOfferRepository.deleteAllByExpirationDateBefore(LocalDateTime.now());
    }

    //TODO OGARNĄĆ MIASTA I WOJEWÓDZTWA BO ŚMIETNIK
    //TODO WALUTY CONVERTER
   @Scheduled(cron = "0 30 6 * * *")
    public void matchOffersAndSendToUsers() {
        for (UserEntity user : userEntityRepository.findAll()) {
            Set<UnifiedOfferEntity> matchedOffers = new HashSet<>();
            Set<UnifiedOfferEntity> unifiedOfferEntities = unifedOfferRepository.findAll().stream()
                    .filter(unifiedOfferEntity -> !user.getMatchedOffers().contains(unifiedOfferEntity))
                    .collect(Collectors.toSet());

            for (UnifiedOfferEntity unifiedOfferEntity : unifiedOfferEntities) {
                if (unifiedOfferEntity.getSkills().containsAll(user.getUserPreferences().getOwnedSkills()) &&
                        unifiedOfferEntity.getSeniority().containsAll(user.getUserPreferences().getOwnedSeniority())) {
                    for (ContractDetailsEntity contractDetail : unifiedOfferEntity.getContractDetails()) {
                        if (contractDetail.getSalaryFrom() >= user.getUserPreferences().getSalaryMin() && (
                                (user.getUserPreferences().isUop() && contractDetail.getTypeOfContract().toString().equals("UOP"))
                                        || user.getUserPreferences().isB2b() && contractDetail.getTypeOfContract().toString().equals("B2B"))) {
                            matchedOffers.add(unifiedOfferEntity);
                        }
                    }
                }
            }
            user.getMatchedOffers().addAll(matchedOffers);
            userEntityRepository.saveAndFlush(user);

            emailSenderService.sendEmail(user.getEmail(), "oferty", matchedOffers.toString());
        }
    }

    private void checkIfEverySkillExistInDB() {
        List<String> listOfSkill = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Skills.txt"))) {
            listOfSkill = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        listOfSkill.removeAll(skillEntityRepository.getAllSkillNames());

        if (listOfSkill.size() > 0) {
            for (String s : listOfSkill) {
                skillEntityRepository.saveAndFlush(new SkillEntity(s));
            }
        }
    }

    private void checkIfEverySeniorityExistInDB() {
        List<String> listOfSeniorities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/Seniority.txt"))) {
            listOfSeniorities = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        listOfSeniorities.removeAll(seniorityEntityRepository.getAllSeniorityNames());

        if (listOfSeniorities.size() > 0) {
            for (String s : listOfSeniorities) {
                seniorityEntityRepository.saveAndFlush(new SeniorityEntity(s));
            }
        }
    }


}
