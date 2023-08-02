package com.ssafy.jarviser.meeting;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Participant;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.RequestUserDto;
import com.ssafy.jarviser.service.MeetingService;
import com.ssafy.jarviser.service.UserService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
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
    @DisplayName("미팅 등록 테스트")
    @Transactional
    @Rollback(value = false)
    void testMeetingInsertByMeetingId() throws Exception{

        //유저가 참여한 미팅 등록
        Meeting meeting = Meeting.builder()
                .meetingName("testMeeting")
                .meetingUrl("www.ssafy.com/sampleurl")
                .hostId(123L)
                .startTime(LocalDateTime.now())
                .build();


        //when
        long meetingId = ms.insertMeeting(meeting);
        Meeting findMeeting = ms.getMeeting(meetingId);

        //then
        Assertions.assertThat(findMeeting).isEqualTo(meeting);

    }

    @Test
    @DisplayName("미팅 참여 테스트")
    @Transactional
    @Rollback(value = false)
    void testJoinMeeting() throws Exception{
        //given
        //미팅룸 개설
        Meeting meeting1 = Meeting.builder()
                .meetingName("testMeeting1")
                .meetingUrl("www.ssafy.com/sampleurl11")
                .hostId(123L)
                .startTime(LocalDateTime.now())
                .build();

        Meeting meeting2 = Meeting.builder()
                .meetingName("testMeeting2")
                .meetingUrl("www.ssafy.com/sampleurl22")
                .hostId(113L)
                .startTime(LocalDateTime.now())
                .build();

        Meeting meeting3 = Meeting.builder()
                .meetingName("testMeeting3")
                .meetingUrl("www.ssafy.com/sampleurl33")
                .hostId(10L)
                .startTime(LocalDateTime.now())
                .build();
        //미팅룸 DB 등록
        ms.insertMeeting(meeting1);
        ms.insertMeeting(meeting2);
        ms.insertMeeting(meeting3);
        //회원 DB등록
        RequestUserDto requestUserDto1 = RequestUserDto.builder().name("kim").password("kim1234").email("kim1234@gmail.com").build();
        us.regist(requestUserDto1);
        User user1 = us.getUser(requestUserDto1.getEmail());

        RequestUserDto requestUserDto2 = RequestUserDto.builder().name("choi").password("choi4321").email("choi4321@gmail.com").build();
        us.regist(requestUserDto2);
        User user2 = us.getUser(requestUserDto2.getEmail());
        RequestUserDto requestUserDto3 = RequestUserDto.builder().name("park").password("abcpark").email("abcpark9393@gmail.com").build();
        us.regist(requestUserDto3);
        User user3 = us.getUser(requestUserDto3.getEmail());

        //when

        //회의 참여
//        Participant participant1 = Participant.setParticipant(user1, meeting1);
//        Participant participant2 = Participant.setParticipant(user2, meeting1);
//        Participant participant3 = Participant.setParticipant(user3, meeting1);
//        Participant participant4 = Participant.setParticipant(user1, meeting2);
//        Participant participant5 = Participant.setParticipant(user1, meeting3);
//
//        ms.joinMeeting(participant1);
//        ms.joinMeeting(participant2);
//        ms.joinMeeting(participant3);
//        ms.joinMeeting(participant4);
//        ms.joinMeeting(participant5);
        //then
        List<Meeting> meetings = ms.meetingList(user1.getId());
        List<String> actualNames = new ArrayList<>();
        actualNames.add("testMeeting1");
        actualNames.add("testMeeting2");
        actualNames.add("testMeeting3");

        for(int i = 0 ; i < meetings.size() ;i++){
            String meetingName = meetings.get(i).getMeetingName();
            String actualName = actualNames.get(i);
            Assertions.assertThat(meetingName).isEqualTo(actualName);
        }


    }
}
