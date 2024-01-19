package com.mashreq.online.model;

import com.mashreq.online.validation.TimeFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConferenceRoomRequest {

    @Min(value = 1, message = "Number of persons must be greater than 0")
    @Max(value = 20, message = "Number of persons cannot exceed 20")
    private final int noOfPerson;

    @NonNull
    private final String empId;

    @NonNull
    @TimeFormat
    private final String startTime;

    @NonNull
    @TimeFormat
    private final String endTime;
}
