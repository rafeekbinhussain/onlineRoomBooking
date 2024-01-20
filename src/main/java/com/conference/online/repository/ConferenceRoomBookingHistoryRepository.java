package com.conference.online.repository;

import com.conference.online.repository.daos.ConferenceRoomBookingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceRoomBookingHistoryRepository extends JpaRepository<ConferenceRoomBookingHistoryEntity, Long> {
    List<ConferenceRoomBookingHistoryEntity> findByRoomNameIgnoreCase(String roomName);
}
