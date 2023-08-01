package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Reservation;
import com.ssafy.jarviser.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
