package com.mashreq.online.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull
@Constraint(validatedBy = TimeFormatValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface TimeFormat {
    String message() default "Invalid time format. Use HH:mm";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class TimeFormatValidator implements ConstraintValidator<TimeFormat, String> {
    @Override
    public void initialize(TimeFormat constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are handled by @NotNull annotation
        }

        return value.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$");
    }
}
