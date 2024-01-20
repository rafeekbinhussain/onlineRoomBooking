package com.conference.online.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ConferenceRoomResponse {

    private boolean bookingStatus;

    private final String roomName;

    private final int capacity;

    private final int noOfPerson;

    private final String empId;

    private final String startTime;

    private final String endTime;

}
