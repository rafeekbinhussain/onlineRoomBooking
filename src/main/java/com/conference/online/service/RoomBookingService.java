package com.conference.online.service;


import com.conference.online.model.ConferenceRoomRequest;
import com.conference.online.model.ConferenceRoomResponse;

import java.time.LocalTime;

public interface RoomBookingService {
    ConferenceRoomResponse bookRoom(ConferenceRoomRequest conferenceRoomRequest);

    boolean isTimeSlotAvailable(String roomName, LocalTime startTime, LocalTime endTime);
}
