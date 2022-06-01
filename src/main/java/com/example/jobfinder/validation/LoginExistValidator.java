package com.example.jobfinder.validation;

import com.example.jobfinder.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@RequiredArgsConstructor
public class LoginExistValidator implements ConstraintValidator<LoginExist, String> {
    private final UserEntityRepository userEntityRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return !userEntityRepository.existsByLogin(login);
    }
}
