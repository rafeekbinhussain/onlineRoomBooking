package com.conference.online.repository.daos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
public class ConferenceRoomBookingHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean bookingStatus;

    private String roomName;

    private int noOfPerson;

    private String empId;

    private String startTime;

    private String endTime;

    private LocalDateTime bookingTimestamp;


    public ConferenceRoomBookingHistoryEntity() {
        this.bookingTimestamp = LocalDateTime.now();
    }


}
