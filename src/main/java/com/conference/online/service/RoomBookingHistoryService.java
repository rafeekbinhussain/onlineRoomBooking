package com.conference.online.service;

import com.conference.online.repository.ConferenceRoomBookingHistoryRepository;
import com.conference.online.repository.daos.ConferenceRoomBookingHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBookingHistoryService {

    private final ConferenceRoomBookingHistoryRepository conferenceRoomBookingHistoryRepository;

    @Autowired
    public RoomBookingHistoryService(ConferenceRoomBookingHistoryRepository conferenceRoomBookingHistoryRepository) {
        this.conferenceRoomBookingHistoryRepository = conferenceRoomBookingHistoryRepository;
    }

    public List<ConferenceRoomBookingHistoryEntity> getAllBookedSlots() {
        return conferenceRoomBookingHistoryRepository.findAll();
    }

    public List<ConferenceRoomBookingHistoryEntity> getBookedHistoryByRoomName(String roomName) {
        return conferenceRoomBookingHistoryRepository.findByRoomNameIgnoreCase(roomName);
    }
}
