package com.example.jobfinder.requestBodyModels;

import lombok.Data;

@Data
public class UserRequestBodyModel {
    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
}
