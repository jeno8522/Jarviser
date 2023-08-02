package com.ssafy.jarviser.meeting;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Participant;
import com.ssafy.jarviser.domain.Role;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.service.MeetingService;
import com.ssafy.jarviser.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class MeetingTest {
    @Autowired
    UserService us;
    @Autowired
    MeetingService ms;

    @Test
    @DisplayName("미팅 생성 테스트")
    @Transactional
    @Rollback(value = false)
    void testMeetingInsertByMeetingId() throws Exception{

        //호스트
        RequestUserDto userDto = RequestUserDto.builder()
                .name("host")
                .password("1234")
                .email("host@naver.com")
                .build();

        us.regist(userDto);
        User user = us.findUserByEmail(userDto.getEmail());

        //미팅 생성
        Meeting meeting = Meeting.builder()
                .meetingName("testMeeting")
                .meetingUrl("www.ssafy.com/sampleurl")
                .hostId(123L)
                .startTime(LocalDateTime.now())
                .build();
        //미팅 등록

        //when
        ms.createMeeting(user,meeting);
        //then

        //미팅룸 찾기
        Meeting findMeeting = ms.findMeetingById(meeting.getId());
        //호스트 찾기
        User findHost = us.findUserById(user.getId());

        //미팅 이름 확인
        Assertions.assertThat(findMeeting.getMeetingName()).isEqualTo(meeting.getMeetingName());
        //호스트 이름 확인
        Assertions.assertThat(findHost.getName()).isEqualTo(user.getName());

    }

    @Test
    @DisplayName("특정 미팅의 참여 회원 정보 목록")
    @Transactional
    @Rollback(value = false)
    void testJoinMeeting() throws Exception{
        //given
        //회원 생성
        RequestUserDto wooseokDto = RequestUserDto.builder()
                .name("wooseok")
                .password("1234")
                .email("wooseok@naver.com")
                .build();

        RequestUserDto minwooDto = RequestUserDto.builder()
                .name("minwoo")
                .password("1231421114")
                .email("minwoo@gmail.com")
                .build();

        RequestUserDto changhoonDto = RequestUserDto.builder()
                .name("changhoon")
                .password("123441324")
                .email("changhoon@gmail.com")
                .build();

        RequestUserDto hongwoongDto = RequestUserDto.builder()
                .name("hongwoong")
                .password("123441324")
                .email("hongwoong@gmail.com")
                .build();

        RequestUserDto hyunwoongDto = RequestUserDto.builder()
                .name("hyunwoong")
                .password("123441324")
                .email("hyunwoong@gmail.com")
                .build();

        RequestUserDto taehyunDto = RequestUserDto.builder()
                .name("taehyun")
                .password("123441324")
                .email("taehyun@gmail.com")
                .build();

        //회원가입
        us.regist(wooseokDto);
        us.regist(minwooDto);
        us.regist(changhoonDto);
        us.regist(hongwoongDto);
        us.regist(hyunwoongDto);
        us.regist(taehyunDto);

        User wooseok = us.findUserByEmail(wooseokDto.getEmail());
        User minwoo =  us.findUserByEmail(minwooDto.getEmail());
        User changhoon =  us.findUserByEmail(changhoonDto.getEmail());
        User hongwoong =  us.findUserByEmail(hongwoongDto.getEmail());
        User hyunwoong =  us.findUserByEmail(hyunwoongDto.getEmail());
        User taehyun =  us.findUserByEmail(taehyunDto.getEmail());

        //미팅룸 개설
        Meeting meeting1 = Meeting.builder()
                .meetingName("wooseokMeeting")
                .meetingUrl("www.ssafy.com/wooseok")
                .hostId(wooseok.getId())
                .startTime(LocalDateTime.now())
                .build();



        Meeting meeting2 = Meeting.builder()
                .meetingName("minwooMeeting")
                .meetingUrl("www.ssafy.com/minwoojjang")
                .hostId(minwoo.getId())
                .startTime(LocalDateTime.now())
                .build();

        //wooseok 미팅1 개설
        ms.createMeeting(wooseok,meeting1);
//        //minwoo 미팅2 개설
        ms.createMeeting(minwoo,meeting2);
        //when

//        //회의 참여
        ms.joinMeeting(changhoon,meeting1);
        ms.joinMeeting(hongwoong,meeting2);
        ms.joinMeeting(hyunwoong,meeting1);
        ms.joinMeeting(taehyun,meeting2);
//
//        //then
        List<User> wooseokListByMeetingId = ms.findUserListByMeetingId(meeting1.getId());
        List<User> minwooListByMeetingId = ms.findUserListByMeetingId(meeting2.getId());

        String[] wooseokMeetingUserList = {"wooseok","changhoon","hyunwoong"};
        String[] minwooMeetingUserList = {"minwoo","hongwoong","taehyun"};

        //우석이의 참여방 확인
        for(int i = 0 ; i < wooseokListByMeetingId.size() ;i++){
            String name = wooseokListByMeetingId.get(i).getName();
            String targetName = wooseokMeetingUserList[i];
            Assertions.assertThat(name).isEqualTo(targetName);
        }

        //민우의 참여방 리스트 확인
        for(int i = 0 ; i < minwooListByMeetingId.size() ;i++){
            String name = minwooListByMeetingId.get(i).getName();
            String targetName = minwooMeetingUserList[i];
            Assertions.assertThat(name).isEqualTo(targetName);
        }
    }

    @Test
    @DisplayName("특정 회원의 미팅 참여 리스트 조회")
    @Transactional
    @Rollback(value = false)
    void testMeetingList() throws Exception{
        //given

        //회원 생성
        RequestUserDto wooseokDto = RequestUserDto.builder()
                .name("wooseok")
                .password("1234")
                .email("wooseok@naver.com")
                .build();

        RequestUserDto minwooDto = RequestUserDto.builder()
                .name("minwoo")
                .password("1231421114")
                .email("minwoo@gmail.com")
                .build();

        RequestUserDto changhoonDto = RequestUserDto.builder()
                .name("changhoon")
                .password("123441324")
                .email("changhoon@gmail.com")
                .build();

        RequestUserDto hongwoongDto = RequestUserDto.builder()
                .name("hongwoong")
                .password("123441324")
                .email("hongwoong@gmail.com")
                .build();

        RequestUserDto hyunwoongDto = RequestUserDto.builder()
                .name("hyunwoong")
                .password("123441324")
                .email("hyunwoong@gmail.com")
                .build();

        RequestUserDto taehyunDto = RequestUserDto.builder()
                .name("taehyun")
                .password("123441324")
                .email("taehyun@gmail.com")
                .build();

        //회원가입
        us.regist(wooseokDto);
        us.regist(minwooDto);
        us.regist(changhoonDto);
        us.regist(hongwoongDto);
        us.regist(hyunwoongDto);
        us.regist(taehyunDto);

        User wooseok = us.findUserByEmail(wooseokDto.getEmail());
        User minwoo =  us.findUserByEmail(minwooDto.getEmail());
        User changhoon =  us.findUserByEmail(changhoonDto.getEmail());
        User hongwoong =  us.findUserByEmail(hongwoongDto.getEmail());
        User hyunwoong =  us.findUserByEmail(hyunwoongDto.getEmail());
        User taehyun =  us.findUserByEmail(taehyunDto.getEmail());

        //미팅룸 개설
        //미팅룸 개설

        Meeting meeting2 = Meeting.builder()
                .meetingName("minwooMeeting")
                .meetingUrl("www.ssafy.com/minwoojjang")
                .hostId(minwoo.getId())
                .startTime(LocalDateTime.now())
                .build();

        Meeting meeting3 = Meeting.builder()
                .meetingName("changhoonMeeting")
                .meetingUrl("www.ssafy.com/changhoon")
                .hostId(wooseok.getId())
                .startTime(LocalDateTime.now())
                .build();



        Meeting meeting4 = Meeting.builder()
                .meetingName("hongwoongMeeting")
                .meetingUrl("www.ssafy.com/hongwoong")
                .hostId(minwoo.getId())
                .startTime(LocalDateTime.now())
                .build();

        Meeting meeting5 = Meeting.builder()
                .meetingName("hyunwoongMeeting")
                .meetingUrl("www.ssafy.com/hyunwoong")
                .hostId(wooseok.getId())
                .startTime(LocalDateTime.now())
                .build();


        Meeting meeting6 = Meeting.builder()
                .meetingName("taehyunMeeting")
                .meetingUrl("www.ssafy.com/taehyun")
                .hostId(minwoo.getId())
                .startTime(LocalDateTime.now())
                .build();

        //미팅룸 개설
        ms.createMeeting(minwoo,meeting2);
        ms.createMeeting(changhoon,meeting3);
        ms.createMeeting(hongwoong,meeting4);
        ms.createMeeting(hyunwoong,meeting5);
        ms.createMeeting(taehyun,meeting6);


        //when
        //우석이의 다국적 미팅참여
        ms.joinMeeting(wooseok,meeting2);
        ms.joinMeeting(wooseok,meeting3);
        ms.joinMeeting(wooseok,meeting4);
        ms.joinMeeting(wooseok,meeting5);
        ms.joinMeeting(wooseok,meeting6);
        //then
        //우석이의 모든 미팅 참여내역 확인
        String[] targetWooseokAttendanceMeeting = {"minwooMeeting","changhoonMeeting","hongwoongMeeting","hyunwoongMeeting","taehyunMeeting"};
        List<Meeting> wooseokAttendacneMeetings = ms.findMeetingListByUserId(wooseok.getId());

        for(int i = 0 ; i<wooseokAttendacneMeetings.size();i++){
            String name = wooseokAttendacneMeetings.get(i).getMeetingName();
            String targetName = targetWooseokAttendanceMeeting[i];
            Assertions.assertThat(name).isEqualTo(targetName);
        }
    }
}
