package com.example.jobfinder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jobfinderservice2022@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmailWithActivationLink(String receiverEmail, String receiverLogin, String id) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jobfinderservice2022@gmail.com");
        message.setTo(receiverEmail);
        message.setSubject("Job Finder - aktywuj swoje konto");
        String link =  "http://localhost:8080/" + id + "/activate";
        String body = "Witaj " + receiverLogin + ". Ten adres email został podany przy zakładaniu konta\n" +
                "Dokończ rejestrację otwierając link: "+ link;
        message.setText(body);

        mailSender.send(message);
    }

    /*
    @GetMapping("/sendTest")
    public void sendMailTest() {
        String toEmail = "rsuski7@wp.pl";
        String subject = "Test";
        String body = "Test body";

        emailSenderService.sendEmail(toEmail, subject, body);
    }
     */

}
