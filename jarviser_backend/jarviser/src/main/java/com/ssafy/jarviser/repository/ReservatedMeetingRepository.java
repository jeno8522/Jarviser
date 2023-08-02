package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.ReservatedMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservatedMeetingRepository extends JpaRepository<ReservatedMeeting, Long> {
    Optional<ReservatedMeeting> findById(Long id);
    void deleteById(Long id);

}
