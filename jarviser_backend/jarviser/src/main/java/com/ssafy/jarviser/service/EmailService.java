package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.ReservatedMeeting;
import com.ssafy.jarviser.domain.Reservation;
import com.ssafy.jarviser.repository.ReservatedMeetingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final ReservatedMeetingRepository reservatedMeetingRepository;

    //TODO: 리팩토링할 것
    public void sendEmail(String title, String description, List<String> emails){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("안녕하세요, 당신의 비서 Jarviser입니다.");
        message.setTo(emails.toString());
        message.setText("미팅명 : "+ title + "\n\n" + "상세: " + description);

        javaMailSender.send(message);
    }

    // CONSIDER
    //1. 모두 읽어들인 후 비즈니스 로직으로 10분 이내로 남은걸 뽑아낼 것이냐
    //2. 쿼리에서 10분 이내로 남은걸 뽑아낼 것이냐
    @Scheduled(fixedRate = 120*1000)
    public void checkReservationTask(){
        LocalDateTime curTime = LocalDateTime.now();
        List<ReservatedMeeting> reservatedMeetings =
                reservatedMeetingRepository.findByStartTimeIsBetween(curTime, curTime.plusMinutes(10));

        if(reservatedMeetings.size() > 0){
            reservatedMeetings.forEach(this::getMailContent);
        }
    }

    // CONSIDER: 굳이 transactional을 걸어야할까?
    @Transactional
    public void getMailContent(ReservatedMeeting reservatedMeeting) {
        String title = reservatedMeeting.getMeetingName();
        String description = reservatedMeeting.getDescription();
        List<Reservation> reservations = reservatedMeeting.getReservations();

        List<String> emails = reservations.stream()
                .map(reservation -> reservation.getUser().getEmail())
                .toList();

        sendEmail(title,description,emails);
    }
}