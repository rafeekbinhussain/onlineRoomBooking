package com.conference.online.controller;

import com.conference.online.service.RoomBookingService;
import com.conference.online.model.ConferenceRoomRequest;
import com.conference.online.model.ConferenceRoomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conference-room")
public class ConferenceBookingController extends BaseController {

    private final RoomBookingService roomService;

    @Autowired
    public ConferenceBookingController(RoomBookingService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/book-room")
    public ResponseEntity<?> bookConferenceRoom(@Valid @RequestBody ConferenceRoomRequest conferenceRoomRequest, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(this::formatFieldError)
                    .toList();
            return createResponseEntity(HttpStatus.BAD_REQUEST, createErrorResponse(HttpStatus.BAD_REQUEST, "Validation error", errorMessages));
        }

        try {
            ConferenceRoomResponse conferenceRoomResponse = roomService.bookRoom(conferenceRoomRequest);
            return createResponseEntity(HttpStatus.CREATED, conferenceRoomResponse);
        } catch (Exception e) {
            return createResponseEntity(HttpStatus.OK, createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing the request", e.getMessage()));
        }
    }

    private String formatFieldError(FieldError error) {
        return String.format("%s %s", error.getField(), error.getDefaultMessage());
    }
}
