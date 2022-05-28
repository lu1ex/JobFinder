package com.example.jobfinder;

import com.example.jobfinder.servicesForDownloaders.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobFinderApplication {
    @Autowired
    private EmailSenderService emailSenderService;

    public static void main(String[] args) {
        SpringApplication.run(JobFinderApplication.class, args);
    }
}
