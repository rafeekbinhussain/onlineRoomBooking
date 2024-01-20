package com.conference.online.validation;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalTime;
import java.util.Scanner;

public class ValidationUtils {

    public static void validateTimeFormat(String time, String errorMessage) {
        try (var scanner = new Scanner(time)) {
            scanner.useDelimiter(":");
            int hours = scanner.nextInt();
            int minutes = scanner.nextInt();
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || scanner.hasNext()) {
                throw new ConstraintViolationException("Invalid request content.", null);
            }
        } catch (ConstraintViolationException e) {
            throw new ConstraintViolationException(errorMessage, null);
        }
    }

    public static void validateBookingTimeSlots(LocalTime startTime, LocalTime endTime) {
        if (isRoundedSlot(startTime) || isRoundedSlot(endTime) || !isBookingIntervalValid(startTime, endTime)) {
            throw new IllegalArgumentException("Booking time slots must be in 15-minute intervals and aligned to the nearest hour.");
        }

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time and end time cannot be the same.");
        }
    }

    private static boolean isRoundedSlot(LocalTime time) {
        int minutes = time.getMinute();
        return minutes % 15 != 0 || (minutes != 0 && minutes != 15 && minutes != 30 && minutes != 45);
    }

    private static boolean isBookingIntervalValid(LocalTime startTime, LocalTime endTime) {
        long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
        return minutes % 15 == 0 && minutes >= 15 && minutes <= 60;
    }

    private ValidationUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
