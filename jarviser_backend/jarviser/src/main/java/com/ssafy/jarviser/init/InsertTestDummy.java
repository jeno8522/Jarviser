package com.ssafy.jarviser.init;

import com.ssafy.jarviser.domain.*;
import com.ssafy.jarviser.repository.MeetingRepository;
import com.ssafy.jarviser.repository.ReservatedMeetingRepository;
import com.ssafy.jarviser.repository.ReservationRepository;
import com.ssafy.jarviser.repository.UserRepository;
import com.ssafy.jarviser.service.MeetingService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class InsertTestDummy implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservatedMeetingRepository reservatedMeetingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 여기에 애플리케이션 시작 시에 실행할 쿼리를 작성하세요.
        User taehyun = User.builder()
                .email("taehyun0121@naver.com")
                .password(passwordEncoder.encode("password"))
                .name("taehyun")
                .role(Role.USER)
                .build();
        userRepository.save(taehyun);

        User hongwoong = User.builder()
                .email("ansghddnd12@naver.com")
                .password(passwordEncoder.encode("password"))
                .name("hongwoong")
                .role(Role.USER)
                .build();
        userRepository.save(hongwoong);

        ReservatedMeeting meeting = ReservatedMeeting.builder()
                .meetingName("test")
                .hostId(taehyun.getId())
                .description("인생은 한방이다 - copilot")
                .startTime(LocalDateTime.now().plusMinutes(10))
                .build();
        reservatedMeetingRepository.save(meeting);

        Reservation reservation1 = new Reservation();
        reservation1.setReservation(taehyun, meeting);
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setReservation(hongwoong, meeting);
        reservationRepository.save(reservation2);


        //회원 가입
        User test = User.builder()
                .email("test@gmail.com")
                .password(passwordEncoder.encode("test"))
                .name("testtesttest")
                .role(Role.USER)
                .build();

        userRepository.save(test);

        User wooseok = User.builder()
                .email("wooseok777777@gmail.com")
                .password(passwordEncoder.encode("wooseokPassword"))
                .name("ChoiWooSeok")
                .role(Role.USER)
                .build();

        userRepository.save(wooseok);

        User minwoo = User.builder()
                .email("jerry142857@naver.com")
                .password(passwordEncoder.encode("minwooPassword"))
                .name("KimMinWoo")
                .role(Role.USER)
                .build();
        userRepository.save(minwoo);

        User woong = User.builder()
                .email("woongwoong@naver.com")
                .password(passwordEncoder.encode("woongPassword"))
                .name("NaHyunWoong")
                .role(Role.USER)
                .build();
        userRepository.save(woong);

        User changHoon = User.builder()
                .email("ReptileHoon@naver.com")
                .password(passwordEncoder.encode("changhoon"))
                .name("JooChangHoon")
                .role(Role.USER)
                .build();
        userRepository.save(changHoon);

        // 테스트 미팅 생성
        String testMeetingName = "test's Meeting";
        Meeting testMeeting = meetingService.createMeeting(test.getId(),testMeetingName);

        // 우석 미팅 생성
        String wooseokMeetingName = "wooseok's Meeting";
        Meeting wooseokMeeting = meetingService.createMeeting(wooseok.getId(),wooseokMeetingName);

        // 민우 미팅 생성
        String minWooMeetingName = "minwoo's Meeting";
        Meeting minwooMeeting = meetingService.createMeeting(minwoo.getId(),minWooMeetingName);

        // 홍웅 미팅 생성
        String hongWoongMeetingName = "hongwoong's Meeting";
        Meeting hongWoongMeeting = meetingService.createMeeting(hongwoong.getId(),hongWoongMeetingName);


        //테스트 미팅에 우석-민우-홍웅-태현-현웅 참여
        meetingService.joinMeeting(wooseok.getId(),testMeeting);
        meetingService.joinMeeting(minwoo.getId(),testMeeting);
        meetingService.joinMeeting(hongwoong.getId(),testMeeting);
        meetingService.joinMeeting(taehyun.getId(),testMeeting);
        meetingService.joinMeeting(woong.getId(),testMeeting);
        //우석미팅에 현웅 - 창훈 - 테스트 참여
        meetingService.joinMeeting(woong.getId(),wooseokMeeting);
        meetingService.joinMeeting(changHoon.getId(), wooseokMeeting);
        meetingService.joinMeeting(test.getId(),wooseokMeeting);

        //민우 미팅에 우석 - 태현 - 홍웅 - 테스트 참여
        meetingService.joinMeeting(wooseok.getId(),minwooMeeting);
        meetingService.joinMeeting(taehyun.getId(), minwooMeeting);
        meetingService.joinMeeting(hongwoong.getId(), minwooMeeting);
        meetingService.joinMeeting(wooseok.getId(),minwooMeeting);
        meetingService.joinMeeting(test.getId(),minwooMeeting);

        //홍웅 미팅에 테스트 참여
        meetingService.joinMeeting(test.getId(),hongWoongMeeting);

        //테스트 미팅에서 발화


        try (InputStream file = getClass().getResourceAsStream("/MeetingDummyFile/meeting_notes.xlsx");) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell speakerCell = row.getCell(0);
                Cell contentCell = row.getCell(1);

                String speaker = speakerCell != null ? speakerCell.getStringCellValue().trim() : "";
                String content = contentCell != null ? contentCell.getStringCellValue().trim() : "";
                if(content.equals("내용"))continue;

                AudioMessage audioMessage = AudioMessage.builder()
                                                .userName(speaker)
                                                        .content(content)
                                                                .speechLength(content.length())
                                                                        .meeting(testMeeting)
                                                                                .build();

                meetingService.addAudioMessageToMeeting(testMeeting.getId(),audioMessage);
                System.out.println("발화자: " + speaker);
                System.out.println("내용: " + content);
                System.out.println("=======================");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}