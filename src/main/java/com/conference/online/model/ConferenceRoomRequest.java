package com.conference.online.model;

import com.conference.online.validation.TimeFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.NonNull;

public record ConferenceRoomRequest(
        @Min(value = 1, message = "Number of persons must be greater than 0")
        @Max(value = 20, message = "Number of persons cannot exceed 20")
        int noOfPerson,

        @NonNull String empId,

        @NonNull @TimeFormat String startTime,

        @NonNull @TimeFormat String endTime) {


}
