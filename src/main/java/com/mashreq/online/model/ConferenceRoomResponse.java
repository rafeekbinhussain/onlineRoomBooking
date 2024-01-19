package com.mashreq.online.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConferenceRoomResponse {

    private boolean bookingStatus;

    private final String roomName;

    private final int noOfPerson;

    private final String empId;

    private final String startTime;

    private final String endTime;

}
