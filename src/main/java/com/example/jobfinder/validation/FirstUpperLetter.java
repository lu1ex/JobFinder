package com.example.jobfinder.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FirstUpperLetterValidator.class)
public @interface FirstUpperLetter {
    String message() default "Pierwsza litera musi być dużą literą";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

