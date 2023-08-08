package com.ssafy.jarviser.init;

import com.ssafy.jarviser.domain.ReservatedMeeting;
import com.ssafy.jarviser.domain.Reservation;
import com.ssafy.jarviser.domain.Role;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.repository.ReservatedMeetingRepository;
import com.ssafy.jarviser.repository.ReservationRepository;
import com.ssafy.jarviser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InsertTestDummy implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservatedMeetingRepository reservatedMeetingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 여기에 애플리케이션 시작 시에 실행할 쿼리를 작성하세요.
        User user1 = User.builder()
                .email("taehyun0121@naver.com")
                .password(passwordEncoder.encode("password"))
                .name("User")
                .role(Role.USER)
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .email("ansghddnd12@naver.com")
                .password("password")
                .name("User")
                .role(Role.USER)
                .build();
        userRepository.save(user2);

        ReservatedMeeting meeting = ReservatedMeeting.builder()
                .meetingName("test")
                .hostId(user1.getId())
                .description("인생은 한방이다 - copilot")
                .startTime(LocalDateTime.now().plusMinutes(10))
                .build();
        reservatedMeetingRepository.save(meeting);

        Reservation reservation1 = new Reservation();
        reservation1.setReservation(user1, meeting);
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setReservation(user2, meeting);
        reservationRepository.save(reservation2);

    }
}