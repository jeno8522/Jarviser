package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.*;
import com.ssafy.jarviser.dto.ResponseMeetingStatics;
import com.ssafy.jarviser.repository.MeetingRepository;
import com.ssafy.jarviser.repository.ParticipantRepository;
import com.ssafy.jarviser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImp implements MeetingService{

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;


    @Override
    public void createMeeting(User host, Meeting meeting) {
        //이시점에서 미팅이 생성되므로 DB에 미팅저장
        meetingRepository.saveMeeting(meeting);
        //미팅 - 참여자(호스트) 생성
        Participant participant = Participant.participate(host, meeting);
        //호스트로 참여자 설정
        participant.setRole(ParticipantRole.HOST);
        //미팅 - 참여자(호스트) 저장
        participantRepository.joinParticipant(participant);

    }

    @Override
    public void joinMeeting(User user, Meeting meeting) {
        //참여자 - 미팅 생성
        Participant participant = Participant.participate(user, meeting);
        //참여자로 참여자 설정
        participant.setRole(ParticipantRole.PARTICIPANT);
        //참여자 미팅 저장
        participantRepository.joinParticipant(participant);
    }

    @Override
    public Meeting findMeetingById(long meetingId) {
        return meetingRepository.findMeetingById(meetingId);
    }

    @Override
    public List<User> findUserListByMeetingId(long meetingId) {
        return meetingRepository.findUserListByMeetingId(meetingId);
    }


    @Override
    public ResponseMeetingStatics meetingStatics(long meetingId) {

        return null;
    }

    @Override
    public List<Meeting> findMeetingListByUserId(long userid) {
        return meetingRepository.findAllMeetingByUserId(userid);
    }

    @Override
    public Report meetingReport(long meetingId) {

        return meetingRepository.findMeetingReportByMeetingId(meetingId);
    }


}
