package com.example.jobfinder.modelDTO;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
}
