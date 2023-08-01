package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.ReservatedMeeting;
import com.ssafy.jarviser.domain.Reservation;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.ReservatedMeetingRepository;
import com.ssafy.jarviser.repository.ReservationRepository;
import com.ssafy.jarviser.repository.ReservationRepositoryCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryCustom reservationRepositoryCustom;
    private final ReservatedMeetingRepository reservatedMeetingRepository;

    @Transactional
    public void createReservation(ReservatedMeeting reservatedMeeting, List<User> users) {
        log.debug(reservatedMeeting.toString());
        reservatedMeetingRepository.save(reservatedMeeting);

        for (User user : users) {
            Reservation reservation = new Reservation();
            reservation.setReservation(user, reservatedMeeting);
            reservationRepository.save(reservation);
        }
    }
}
