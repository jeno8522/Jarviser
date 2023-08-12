package com.ssafy.jarviser.init;

import com.ssafy.jarviser.domain.*;
import com.ssafy.jarviser.repository.*;
import com.ssafy.jarviser.service.MeetingService;
import com.ssafy.jarviser.service.OpenAIService;
import com.ssafy.jarviser.util.AESEncryptionUtil;
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

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private AESEncryptionUtil aesEncryptionUtil;

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
                .password(passwordEncoder.encode("testtesttest"))
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
        String testMeetingEncrypted = meetingService.createMeeting(test.getId(),testMeetingName);
        long testMeetingId = Long.parseLong(aesEncryptionUtil.decrypt(testMeetingEncrypted));
        Meeting testMeeting = meetingService.findMeetingById(testMeetingId);

        // 우석 미팅 생성
        String wooseokMeetingName = "wooseok's Meeting";
        String wooseokMeetingEncrypted = meetingService.createMeeting(wooseok.getId(),wooseokMeetingName);
        long wooseokMeetingId = Long.parseLong(aesEncryptionUtil.decrypt(wooseokMeetingEncrypted));
        Meeting wooseokMeeting = meetingService.findMeetingById(wooseokMeetingId);

        // 민우 미팅 생성
        String minWooMeetingName = "minwoo's Meeting";
        String minwooMeetingEncrypted = meetingService.createMeeting(minwoo.getId(),minWooMeetingName);
        long minwooMeetingId = Long.parseLong(aesEncryptionUtil.decrypt(minwooMeetingEncrypted));
        Meeting minwooMeeting = meetingService.findMeetingById(minwooMeetingId);

        // 홍웅 미팅 생성
        String hongWoongMeetingName = "hongwoong's Meeting";
        String hongWoongMeetingEncrypted = meetingService.createMeeting(hongwoong.getId(),hongWoongMeetingName);
        long hongWoongMeetingId = Long.parseLong(aesEncryptionUtil.decrypt(hongWoongMeetingEncrypted));
        Meeting hongWoongMeeting = meetingService.findMeetingById(hongWoongMeetingId);

        //테스트 미팅에 우석-민우-홍웅-태현-현웅 참여
        meetingService.joinMeeting(wooseok.getId(),testMeetingId);
        meetingService.joinMeeting(minwoo.getId(),testMeetingId);
        meetingService.joinMeeting(hongwoong.getId(),testMeetingId);
        meetingService.joinMeeting(taehyun.getId(),testMeetingId);
        meetingService.joinMeeting(woong.getId(),testMeetingId);
        //우석미팅에 현웅 - 창훈 - 테스트 참여
        meetingService.joinMeeting(woong.getId(),wooseokMeetingId);
        meetingService.joinMeeting(changHoon.getId(), wooseokMeetingId);
        meetingService.joinMeeting(test.getId(),wooseokMeetingId);

        //민우 미팅에 우석 - 태현 - 홍웅 - 테스트 참여
        meetingService.joinMeeting(wooseok.getId(),minwooMeetingId);
        meetingService.joinMeeting(taehyun.getId(), minwooMeetingId);
        meetingService.joinMeeting(hongwoong.getId(), minwooMeetingId);
        meetingService.joinMeeting(wooseok.getId(),minwooMeetingId);
        meetingService.joinMeeting(test.getId(),minwooMeetingId);

        //홍웅 미팅에 테스트 참여
        meetingService.joinMeeting(test.getId(),hongWoongMeetingId);

        //dummy file path
        String dummyFilePath = "C:\\Users\\SSAFY\\Desktop\\S09P12A506\\jarviser_backend\\jarviser\\src\\main\\resources\\MeetingDummyFile\\meeting_notes.xlsx";
        //테스트 미팅에서 발화저장하기(DB에 실시간으로 저장하는 로직 필요)
        try  {
            FileInputStream file = new FileInputStream(new File(dummyFilePath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell speakerCell = row.getCell(0);
                Cell contentCell = row.getCell(1);

                String speakerId = speakerCell != null ? speakerCell.getStringCellValue().trim() : "";
                String content = contentCell != null ? contentCell.getStringCellValue().trim() : "";
                if(content.equals("내용"))continue;

                long userId = Long.parseLong(speakerId);
                User user = userRepository.findUserById(userId);

                AudioMessage audioMessage = AudioMessage.builder()
                        .user(user)
                                .content(content)
                                        .speechLength(content.length())
                                                .meeting(testMeeting)
                                                        .startTime(LocalDateTime.now())
                                                                .filePath("filePath...")
                                                                        .priority(1)
                                                                                .build();

                meetingService.addAudioMessageToMeeting(testMeeting.getId(),audioMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 오디오 메시지들을 기반으로 키워드 List결과값 가져오기
        List<KeywordStatistics> testMeetingKeywordStatisticsList = meetingService.findAllKeywordStatistics(testMeetingId);


    }
}