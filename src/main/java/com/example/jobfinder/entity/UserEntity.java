package com.example.jobfinder.entity;

import com.example.jobfinder.forms.RegisterForm;
import com.example.jobfinder.requestBodyModels.UserRequestBodyModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id   ;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    //tutaj dorobić OneToOne preferences

    public UserEntity(RegisterForm registerForm) {
        this.id = UUID.randomUUID().toString();
        this.name = registerForm.getName();
        this.surname =  registerForm.getSurname();
        this.login = registerForm.getLogin();
        this.password = registerForm.getPassword();
        this.email = registerForm.getEmail();
    }

    public UserEntity(UserRequestBodyModel userRequestBodyModel) {
        this.id = UUID.randomUUID().toString();
        this.name = userRequestBodyModel.getName();
        this.surname =  userRequestBodyModel.getSurname();
        this.login = userRequestBodyModel.getLogin();
        this.password = userRequestBodyModel.getPassword();
        this.email = userRequestBodyModel.getEmail();
    }
}
