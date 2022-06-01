package com.example.jobfinder.forms;
import com.example.jobfinder.validation.EmailExist;
import com.example.jobfinder.validation.FirstUpperLetter;
import com.example.jobfinder.validation.LoginExist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm {
    @FirstUpperLetter(message = "Imię musi zaczynać się od dużej litery")
    @Size(min = 3, max = 20, message = "Imię może mieć od 3 do 20 znaków")
    private String name;
    @FirstUpperLetter(message = "Nazwisko musi zaczynać się od dużej litery")
    @Size(min = 3, max = 50, message = "Nazwisko może mieć od 3 do 50 znaków")
    private String surname;
    @LoginExist
    private String login;
    @Size(min = 5, message = "Minimalna długość hasła to 5 znaków")
    private String password;
    @EmailExist
    @Email(message = "Sprawdź poprawność adresu e-mail")
    private String email;

}
