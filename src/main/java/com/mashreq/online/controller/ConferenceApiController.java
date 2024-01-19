package com.mashreq.online.controller;

import com.mashreq.online.model.ConferenceRoomRequest;
import com.mashreq.online.model.ConferenceRoomResponse;
import com.mashreq.online.service.ConferenceRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConferenceApiController {

    private final ConferenceRoomService roomService;

    public ConferenceApiController(ConferenceRoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/book-room")
    public ResponseEntity<?> bookConferenceRoom(
            @RequestBody ConferenceRoomRequest conferenceRoomRequest) {
        try {
            ConferenceRoomResponse conferenceRoomResponse = roomService.isRoomAvailable(conferenceRoomRequest);
            return ResponseEntity.ok(conferenceRoomResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
