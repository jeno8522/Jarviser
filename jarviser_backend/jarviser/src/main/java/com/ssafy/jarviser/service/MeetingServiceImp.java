package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Participant;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.ResponseMeetingStatics;
import com.ssafy.jarviser.repository.MeetingRepository;
import com.ssafy.jarviser.repository.ParticipantRepository;
import com.ssafy.jarviser.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImp implements MeetingService{

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public long insertMeeting(Meeting meeting) {
        return meetingRepository.insertMeeting(meeting);
    }

    @Override
    public void joinMeeting(Participant participant) {
        participantRepository.joinParticipant(participant);
    }

    @Override
    public Meeting getMeeting(long meetingId) {
        return meetingRepository.getMeeting(meetingId);
    }


    @Override
    public ResponseMeetingStatics meetingStatics(long meetingId) {

        return null;
    }

    @Override
    public List<Meeting> meetingList(long userid) {
        return meetingRepository.findAllMeetingByUserId(userid);
    }

    @Override
    public Report meetingReport(long userId, long meetingId) {
        return null;
    }


    @Override
    public void delete(long userId, long meetingId) {

    }
}
