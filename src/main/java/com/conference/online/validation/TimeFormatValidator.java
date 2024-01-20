package com.conference.online.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeFormatValidator implements ConstraintValidator<TimeFormat, String> {

    @Override
    public void initialize(TimeFormat constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.matches("^([01]?\\d|2[0-3]):[0-5]\\d$");
    }
}
