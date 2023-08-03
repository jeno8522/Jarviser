package com.ssafy.jarviser.meeting;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.service.MeetingService;
import com.ssafy.jarviser.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
public class MeetingTest {
    @Autowired UserService us;
    @Autowired MeetingService ms;

    @Test
    @DisplayName("MeetingCreateTest")
    @Transactional
    @Rollback(value = false)
    void testMeetingInsertByMeetingId() throws Exception{

        //given 호스트
        RequestUserDto userDto = RequestUserDto.builder()
                .name("host")
                .password("1234")
                .email("host@naver.com")
                .build();

        Long id = us.regist(userDto);

        //when 미팅 생성
        Meeting meeting = ms.createMeeting(id,"wooseokMeeting");


        //then

        //미팅룸 찾기
        Meeting findMeeting = ms.findMeetingById(meeting.getId());
        //호스트 찾기
        User findHost = us.findUserById(id);

        //미팅 이름 확인
        Assertions.assertThat(findMeeting.getMeetingName()).isEqualTo(meeting.getMeetingName());
        //호스트 이름 확인
        Assertions.assertThat(findHost.getName()).isEqualTo(userDto.getName());

    }

    @Test
    @DisplayName("List of Users who joined the meeting")
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
        Long wooseokId = us.regist(wooseokDto);
        Long minwooId = us.regist(minwooDto);
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

        //미팅 생성
        //wooseok 미팅1 개설
        Meeting wooseokMeeting = ms.createMeeting(wooseokId, "wooseokMeeting");
//        //minwoo 미팅2 개설
        Meeting minwooMeeting = ms.createMeeting(minwooId, "minwooMeeting");
        //when

//        //회의 참여
        ms.joinMeeting(changhoon.getId(),wooseokMeeting);
        ms.joinMeeting(hongwoong.getId(),minwooMeeting);
        ms.joinMeeting(hyunwoong.getId(),wooseokMeeting);
        ms.joinMeeting(taehyun.getId(),minwooMeeting);
//
//        //then
        List<User> wooseokListByMeetingId = ms.findUserListByMeetingId(wooseokMeeting.getId());
        List<User> minwooListByMeetingId = ms.findUserListByMeetingId(minwooMeeting.getId());

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
    @DisplayName("List of meetings that the user has joined")
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
        Long wooseokId = us.regist(wooseokDto);
        Long minwooId = us.regist(minwooDto);
        Long changhoonId = us.regist(changhoonDto);
        Long hongwoongId = us.regist(hongwoongDto);
        Long hyunwoongId = us.regist(hyunwoongDto);
        Long taehyunId = us.regist(taehyunDto);


        //미팅룸 개설

        Meeting minwooMeeting = ms.createMeeting(minwooId, "minwooMeeting");
        Meeting changhoonMeeting = ms.createMeeting(changhoonId, "changhoonMeeting");
        Meeting hongwoongMeeting = ms.createMeeting(hongwoongId, "hongwoongMeeting");
        Meeting hyunwoongMeeting = ms.createMeeting(hyunwoongId, "hyunwoongMeeting");
        Meeting taehyunMeeting = ms.createMeeting(taehyunId, "taehyunMeeting");
        //미팅룸 개설



        //when
        //우석이의 다국적 미팅참여
        ms.joinMeeting(wooseokId,minwooMeeting);
        ms.joinMeeting(wooseokId,changhoonMeeting);
        ms.joinMeeting(wooseokId,hongwoongMeeting);
        ms.joinMeeting(wooseokId,hyunwoongMeeting);
        ms.joinMeeting(wooseokId,taehyunMeeting);
        //then
        //우석이의 모든 미팅 참여내역 확인
        String[] targetWooseokAttendanceMeeting = {"minwooMeeting","changhoonMeeting","hongwoongMeeting","hyunwoongMeeting","taehyunMeeting"};
        List<Meeting> wooseokAttendacneMeetings = ms.findMeetingListByUserId(wooseokId);

        for(int i = 0 ; i<wooseokAttendacneMeetings.size();i++){
            String name = wooseokAttendacneMeetings.get(i).getMeetingName();
            String targetName = targetWooseokAttendanceMeeting[i];
            Assertions.assertThat(name).isEqualTo(targetName);
        }
    }
}
