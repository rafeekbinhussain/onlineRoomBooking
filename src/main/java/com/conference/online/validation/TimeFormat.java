package com.conference.online.validation;

import jakarta.validation.Constraint;
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

