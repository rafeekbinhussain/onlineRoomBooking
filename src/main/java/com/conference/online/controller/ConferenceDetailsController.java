package com.conference.online.controller;

import com.conference.online.service.RoomBookingHistoryService;
import com.conference.online.repository.daos.ConferenceRoomBookingHistoryEntity;
import com.conference.online.utils.ConferenceRoomMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conference-room")
public class ConferenceDetailsController extends BaseController {

    private final RoomBookingHistoryService roomBookingHistoryService;

    @Autowired
    public ConferenceDetailsController(RoomBookingHistoryService roomBookingHistoryService) {
        this.roomBookingHistoryService = roomBookingHistoryService;
    }

    @GetMapping("/booked-slots")
    public ResponseEntity<List<ConferenceRoomBookingHistoryEntity>> getAllBookedSlots() {
        List<ConferenceRoomBookingHistoryEntity> bookedSlots = roomBookingHistoryService.getAllBookedSlots();
        return createResponseEntity(HttpStatus.OK, bookedSlots);
    }

    @GetMapping("/booked-history/{roomName}")
    public ResponseEntity<?> getBookedHistoryByRoomName(@PathVariable String roomName) {
        if (ConferenceRoomMapUtil.CONFERENCE_MAP.keySet().stream()
                .map(String::toLowerCase)
                .noneMatch(roomName.toLowerCase()::equals)) {
            return createResponseEntity(HttpStatus.NOT_FOUND,
                    createErrorResponse(HttpStatus.NOT_FOUND, "Room not found", "No booking history found for the specified room."));
        }

        List<ConferenceRoomBookingHistoryEntity> bookedSlots = roomBookingHistoryService.getBookedHistoryByRoomName(roomName);
        return createResponseEntity(HttpStatus.OK, bookedSlots);
    }
}
