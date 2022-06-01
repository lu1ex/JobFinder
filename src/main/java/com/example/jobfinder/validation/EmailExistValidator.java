package com.example.jobfinder.validation;

import com.example.jobfinder.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
@RequiredArgsConstructor
public class EmailExistValidator implements ConstraintValidator<EmailExist, String> {
    private final UserEntityRepository userEntityRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userEntityRepository.existsByEmail(email);
    }
}
