package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.*;
import com.ssafy.jarviser.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservatedMeetingRepository reservatedMeetingRepository;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @Rollback(value = false)
    @BeforeAll
    public void insertDummyData() throws Exception {

        User user = User.builder()
                .email("user@test.com")
                .password("password")
                .name("User")
                .role(Role.USER)
                .build();
        userRepository.save(user);

        ReservatedMeeting meeting = ReservatedMeeting.builder()
                .meetingName("Meeting")
                .hostId(user.getId())
                .startTime(LocalDateTime.now().plusDays(1))
                .build();
        reservatedMeetingRepository.save(meeting);

        for (int i = 10; i <= 20; i++) {
            user = User.builder()
                    .email("user" + i + "@test.com")
                    .password("password" + i)
                    .name("User" + i)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);

            Reservation reservation = new Reservation();
            reservation.setReservation(user, meeting);
            reservationRepository.save(reservation);
        }
    }
}
