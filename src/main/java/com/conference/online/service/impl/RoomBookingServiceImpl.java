package com.conference.online.service.impl;

import com.conference.online.repository.ConferenceRoomBookingHistoryRepository;
import com.conference.online.service.RoomBookingService;
import com.conference.online.model.ConferenceRoomRequest;
import com.conference.online.model.ConferenceRoomResponse;
import com.conference.online.repository.daos.ConferenceRoomBookingHistoryEntity;
import com.conference.online.utils.ConferenceRoomMapUtil;
import com.conference.online.validation.ValidationUtils;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@Transactional
@Singleton
public class RoomBookingServiceImpl implements RoomBookingService {

    private final Map<String, List<LocalTime[]>> bookedTimeSlots;
    private final Map<String, Integer> conferenceMap;
    private final ConferenceRoomBookingHistoryRepository conferenceRoomBookingHistoryRepository;

    public RoomBookingServiceImpl(
            ConferenceRoomBookingHistoryRepository conferenceRoomBookingHistoryRepository) {
        this.conferenceRoomBookingHistoryRepository = conferenceRoomBookingHistoryRepository;
        this.bookedTimeSlots = new HashMap<>();
        this.conferenceMap = ConferenceRoomMapUtil.CONFERENCE_MAP;
    }

    @Override
    public ConferenceRoomResponse bookRoom(ConferenceRoomRequest conferenceRoomRequest) {
        String roomName = findPreferredRoom(conferenceRoomRequest.noOfPerson());
        log.info("Room name chosen: {}", roomName);

        validateTiming(roomName, conferenceRoomRequest.startTime(), conferenceRoomRequest.endTime());
        log.info("Timing validation completed");

        if (!isTimeSlotAvailable(roomName, parseTime(conferenceRoomRequest.startTime()), parseTime(conferenceRoomRequest.endTime()))) {
            log.info("Room already booked as no timeSlot available ");
            throw new IllegalArgumentException("No Slots available on choosing time");
        }

        saveBookingHistory(roomName, conferenceRoomRequest.noOfPerson(), conferenceRoomRequest.startTime(), conferenceRoomRequest.endTime(), conferenceRoomRequest.empId());

        return ConferenceRoomResponse.builder()
                .bookingStatus(true)
                .capacity(conferenceMap.get(roomName))
                .roomName(roomName)
                .noOfPerson(conferenceRoomRequest.noOfPerson())
                .startTime(conferenceRoomRequest.startTime())
                .empId(conferenceRoomRequest.empId())
                .endTime(conferenceRoomRequest.endTime())
                .build();
    }

    private void validateTiming(String roomName, String startTime, String endTime) {
        ValidationUtils.validateTimeFormat(startTime, "Invalid startTime format. Use HH:mm");
        ValidationUtils.validateTimeFormat(endTime, "Invalid endTime format. Use HH:mm");
        LocalTime start = parseTime(startTime);
        LocalTime end = parseTime(endTime);
        ValidationUtils.validateBookingTimeSlots(start, end);
        if (hasOverlappingBooking(roomName, start, end)) {
            throw new IllegalArgumentException("Room already booked with mentioned time frame");
        }
    }

    @Override
    public boolean isTimeSlotAvailable(String roomName, LocalTime startTime, LocalTime endTime) {
        if (!bookedTimeSlots.containsKey(roomName)) {
            return true;
        }

        for (LocalTime[] bookedSlot : bookedTimeSlots.get(roomName)) {
            LocalTime bookedStartTime = bookedSlot[0];
            LocalTime bookedEndTime = bookedSlot[1];

            if (!(endTime.isBefore(bookedStartTime) || startTime.isAfter(bookedEndTime))) {
                log.error("Invalid time slot chosen");
                throw new IllegalArgumentException("Invalid time slot chosen");
            }
        }
        return true;
    }

    private String findPreferredRoom(int noOfPersons) {
        return conferenceMap.entrySet().stream()
                .filter(entry -> noOfPersons <= entry.getValue())
                .min(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalArgumentException("No room available for the specified conditions"));
    }

    private void saveBookingHistory(String roomName, int noOfPerson, String startTime, String endTime, String empId) {
        List<LocalTime[]> roomTimeSlots = bookedTimeSlots.computeIfAbsent(roomName, k -> new ArrayList<>());
        LocalTime[] newBooking = {parseTime(startTime), parseTime(endTime)};
        roomTimeSlots.add(newBooking);

        ConferenceRoomBookingHistoryEntity bookingHistory = ConferenceRoomBookingHistoryEntity.builder()
                .bookingStatus(true)
                .roomName(roomName)
                .noOfPerson(noOfPerson)
                .empId(empId)
                .startTime(startTime)
                .endTime(endTime)
                .bookingTimestamp(LocalDateTime.now())
                .build();

        conferenceRoomBookingHistoryRepository.save(bookingHistory);
    }

    private LocalTime parseTime(String timeString) {
        return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
    }

    private boolean hasOverlappingBooking(String roomName, LocalTime checkStartTime, LocalTime checkEndTime) {
        if (!bookedTimeSlots.containsKey(roomName)) {
            return false;
        }

        for (LocalTime[] bookedSlot : bookedTimeSlots.get(roomName)) {
            LocalTime bookedStartTime = bookedSlot[0];
            LocalTime bookedEndTime = bookedSlot[1];

            if (!(checkEndTime.isBefore(bookedStartTime) || checkStartTime.isAfter(bookedEndTime))) {
                return true;
            }
        }
        return false;
    }
}
