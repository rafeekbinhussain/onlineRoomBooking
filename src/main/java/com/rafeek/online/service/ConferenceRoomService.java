package com.rafeek.online.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rafeek.online.model.ConferenceRoomRequest;
import com.rafeek.online.model.ConferenceRoomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class ConferenceRoomService {

    private final Cache<String, String> caffeineCache;
    private final Map<String, Integer> conferenceMap;

    public ConferenceRoomService() {
        this.caffeineCache = Caffeine.newBuilder().build();
        this.conferenceMap = Map.ofEntries(
                Map.entry("Amaze", 3),
                Map.entry("Beauty", 7),
                Map.entry("Inspire", 12),
                Map.entry("Strive", 20)
        );
    }

    public ConferenceRoomResponse isRoomAvailable(ConferenceRoomRequest conferenceRoomRequest) {
        int noOfPerson = conferenceRoomRequest.getNoOfPerson();
        String startTime = conferenceRoomRequest.getStartTime();
        String endTime = conferenceRoomRequest.getEndTime();
        String empId = conferenceRoomRequest.getEmpId();

        validateTiming(startTime, endTime);
        log.info("Timing validation completed");

        String roomName = findPreferredRoom(noOfPerson);
        log.info("Room name chosen: " + roomName);

        String cacheKey = roomName + startTime + endTime;
        log.info("Room cache key: " + cacheKey);

        // Check cache for availability
        if (Objects.nonNull(caffeineCache.getIfPresent(cacheKey))) {
            throw new IllegalArgumentException("No Slots available on choosing time Room already booked");
        }

        if (!isTimeSlotAvailable(empId, parseTime(startTime), parseTime(endTime))) {
            log.info("Room already booked as no timeSlot available ");
            throw new IllegalArgumentException("No Slots available on choosing time");
        }

        caffeineCache.put(cacheKey, empId);
        log.info("Room booked successfully and cache updated");

        return ConferenceRoomResponse.builder()
                .bookingStatus(true)
                .roomName(roomName)
                .noOfPerson(noOfPerson)
                .startTime(startTime)
                .endTime(endTime)
                .empId(empId)
                .build();
    }

    private void validateTiming(String startTime, String endTime) {
        validateTimeFormatInternal(startTime, "Invalid startTime format. Use HH:mm");
        validateTimeFormatInternal(endTime, "Invalid endTime format. Use HH:mm");

        if (parseTime(startTime).isAfter(parseTime(endTime))) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
    }

    private void validateTimeFormatInternal(String time, String errorMessage) {
        try (var scanner = new Scanner(time)) {
            scanner.useDelimiter(":");
            int hours = scanner.nextInt();
            int minutes = scanner.nextInt();
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || scanner.hasNext()) {
                throw new IllegalArgumentException(errorMessage);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private boolean isTimeSlotAvailable(String empId, LocalTime startTime, LocalTime endTime) {
        long meetingDuration = startTime.until(endTime, ChronoUnit.MINUTES);
        if (meetingDuration != 15) {
            throw new IllegalArgumentException("Meeting Time slot should be 15 minutes and chosen Time is " + meetingDuration);
        }

        if (isOverlappingMaintenance(startTime, endTime) || !isWithinWorkingHours(startTime)) {
            log.error("Time slot chosen on maintenance timings or outside working hours");
            throw new IllegalArgumentException("Time slot chosen on maintenance timings or outside working hours");
        }

        LocalTime checkStartTime = startTime.minusMinutes(15);
        LocalTime checkEndTime = endTime.plusMinutes(15);

        return !hasOverlappingBooking(empId, checkStartTime, checkEndTime);
    }


    private boolean hasOverlappingBooking(String empId, LocalTime checkStartTime, LocalTime checkEndTime) {
        try {
            return caffeineCache.asMap().entrySet().stream()
                    .anyMatch(entry -> {
                        String[] keyParts = entry.getKey().split(":"); // Split key to get start and end times
                        String[] valueParts = entry.getValue().split(":"); // Split value to get empId

                        if (keyParts.length != 2 || valueParts.length != 1) {
                            log.error("Invalid format in cache entry. Key: {}, Value: {}", entry.getKey(), entry.getValue());
                            return false;
                        }

                        String cachedEmpId = valueParts[0];
                        LocalTime bookedStartTime = LocalTime.parse(keyParts[0]);
                        LocalTime bookedEndTime = LocalTime.parse(keyParts[1]);

                        // Check for overlaps considering the 15-minute interval and matching empId
                        return cachedEmpId.equals(empId) &&
                                !(checkEndTime.isBefore(bookedStartTime.minusMinutes(15))
                                        || checkStartTime.isAfter(bookedEndTime.plusMinutes(15)))
                                && !(checkEndTime.isAfter(bookedStartTime.plusMinutes(15))
                                || checkStartTime.isBefore(bookedEndTime.plusMinutes(15)));
                    });
        } catch (Exception e) {
            log.error("Error checking overlapping booking", e);
            return false;
        }
    }


    private boolean isOverlappingMaintenance(LocalTime startTime, LocalTime endTime) {
        return Arrays.stream(maintenanceTimings)
                .anyMatch(maintenanceStart -> !endTime.isBefore(maintenanceStart)
                        && !startTime.isAfter(maintenanceStart.plusMinutes(15)));
    }

    private boolean isWithinWorkingHours(LocalTime time) {
        return time.isAfter(LocalTime.of(9, 0)) && time.isBefore(LocalTime.of(18, 0));
    }

    private String findPreferredRoom(int noOfPersons) {
        return conferenceMap.entrySet().stream()
                .filter(entry -> noOfPersons <= entry.getValue())
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("No room available for the specified conditions"));
    }

    private LocalTime parseTime(String timeString) {
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
    }

    private final LocalTime[] maintenanceTimings = {
            LocalTime.of(9, 0), LocalTime.of(9, 15),
            LocalTime.of(13, 0), LocalTime.of(13, 15),
            LocalTime.of(17, 0), LocalTime.of(17, 15)
    };
}

