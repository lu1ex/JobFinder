package com.example.jobfinder.controller;

import com.example.jobfinder.entity.UnifiedOfferEntity;
import com.example.jobfinder.repository.UnifedOfferRepository;
import com.example.jobfinder.services.UnifiedOfferService;
import com.example.jobfinder.servicesForDownloaders.EmailSenderService;
import com.example.jobfinder.servicesForDownloaders.JobFinderService;
import com.example.jobfinder.servicesForDownloaders.SkillHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class JobFinderController {
    private final EmailSenderService emailSenderService;
    private final UnifedOfferRepository unifedOfferRepository;
    private final JobFinderService jobFinderService;
    private final UnifiedOfferService unifiedOfferService;
    private final SkillHelper skillHelper;

    @GetMapping("/test")
    public String getOffers() throws ExecutionException, InterruptedException {
        List<UnifiedOfferEntity> result = jobFinderService.getAllOffers();
        for (UnifiedOfferEntity unifiedOfferEntity : result) {
            unifiedOfferEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        }
        unifedOfferRepository.saveAllAndFlush(result);
        return String.valueOf(result.size());
    }

    @GetMapping("/test2")
    public String getNumberOFSkills() {
        List<UnifiedOfferEntity> offers = unifedOfferRepository.findAll();
        UnifiedOfferEntity offer = unifedOfferRepository.findById("4028026e8105e057018105e107d00019").get();

        if (unifedOfferRepository.existsByCityAndStreetAndTitleAndAndCompanyNameAndRemoteAndUrl(
                offer.getCity(), offer.getStreet(), offer.getTitle(), offer.getCompanyName(), offer.isRemote(), offer.getUrl())) {
            return "jest coś takiego";
        }
        return "nie ma";
    }

    @GetMapping("/test3")
    public ResponseEntity<UnifiedOfferEntity> getNumberOFSkills3() {
        return new ResponseEntity<>(unifedOfferRepository.findById("4028026e8105e057018105e1ebff3906").get(), HttpStatus.OK);
    }

    @GetMapping("/sendTest")
    public void sendMailTest() {
        String toEmail = "rsuski7@wp.pl";
        String subject = "Test";
        String body = "Test body";

        emailSenderService.sendEmail(toEmail, subject, body);
    }

    /*
1. pobierz wszystko get /notes
2. pobierz 1 get /notes/{id}
3. dodaj nową post /notes + requestBody - jak admin to z id
4. update put  lub patch /notes
put - requestBody - id  - zastapilem całą encje, jak czegos nie podam to null
patch - jak są nulle to update tylko pola !null
5. delete /notes/{id} <- status 20x


 */
}
