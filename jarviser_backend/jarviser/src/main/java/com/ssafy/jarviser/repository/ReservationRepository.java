package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("select u.email from Reservation r join r.user u where r.id = :reservationId")
    List<String> findEmailsByReservationId(Long reservationId);
}
