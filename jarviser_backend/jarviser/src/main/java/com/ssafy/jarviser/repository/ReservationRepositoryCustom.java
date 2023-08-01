package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Reservation;
import com.ssafy.jarviser.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepositoryCustom {
    List<User> findUsersByReservationId(Long reservationId);
}
