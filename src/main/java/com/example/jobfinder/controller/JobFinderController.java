package com.example.jobfinder.controller;

import com.example.jobfinder.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobFinderController {
    private final EmailSenderService emailSenderService;

    @GetMapping("/sendTest")
    public void sendMailTest() {
        String toEmail = "rsuski7@wp.pl";
        String subject = "Test";
        String body = "Test body";

        emailSenderService.sendEmail(toEmail, subject, body);
    }
}
