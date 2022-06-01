package com.example.jobfinder.validation;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FirstUpperLetterValidator implements ConstraintValidator<FirstUpperLetter, String> {

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("[A-Z|Ł].*");
        Matcher matcher = pattern.matcher(string);

        return matcher.matches();
    }
}