package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.ReservatedMeeting;
import com.ssafy.jarviser.domain.Reservation;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.ReservatedMeetingRepository;
import com.ssafy.jarviser.repository.ReservationRepository;
import com.ssafy.jarviser.repository.ReservationRepositoryCustom;
import com.ssafy.jarviser.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public void createReservation(ReservatedMeeting reservatedMeeting, List<String> userEmails) {
        log.debug(reservatedMeeting.toString());
        reservatedMeetingRepository.save(reservatedMeeting);

        for (String userEmail : userEmails) {
            User user = userRepository.findByEmail(userEmail);
            Reservation reservation = new Reservation();
            reservation.setReservation(user, reservatedMeeting);
            reservationRepository.save(reservation);
        }
    }

    @Transactional
    public List<User> getUsersFromReservatedMeeting(Long reservatedMeetingId) {
        List<User> users = reservationRepositoryCustom.getUsersFromMeetingRoom(reservatedMeetingId);
        return users;
    }

    @Transactional
    public List<ReservatedMeeting> getReservatedMeetings(Long userId) {
        List<ReservatedMeeting> reservatedMeetings = reservationRepositoryCustom.getMeetingsFromUser(userId);
        return reservatedMeetings;
    }

    @Transactional
    public void updateReservation(ReservatedMeeting reservatedMeeting, List<String> userEmails) {
        log.debug(reservatedMeeting.toString());
        reservatedMeetingRepository.save(reservatedMeeting); //save시 이미 있으면 update

        List<Reservation> reservations = reservationRepository.findAllByReservatedMeetingId(reservatedMeeting.getId());
        reservationRepository.deleteAllInBatch(reservations); //select하는 과정은 없고 해오지 않고 오로직 delete만 한다.

        for (String email : userEmails) {
            User user = userRepository.findByEmail(email);
            Reservation reservation = new Reservation();
            reservation.setReservation(user, reservatedMeeting);
            reservationRepository.save(reservation);
        }
    }

    @Transactional
    public void deleteReservation(Long reservatedMeetingId) {
        List<Reservation> reservations = reservationRepository.findAllByReservatedMeetingId(reservatedMeetingId);
        reservationRepository.deleteAllInBatch(reservations); //select하는 과정은 없고 해오지 않고 오로직 delete만 한다.
        reservatedMeetingRepository.deleteById(reservatedMeetingId);
    }
}
