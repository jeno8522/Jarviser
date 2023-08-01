package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.dto.ResponseMeetingStatics;
import com.ssafy.jarviser.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingServiceImp implements MeetingService{
    private final MeetingRepository meetingRepository;

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
    public void update(long userId, long meetingId) {

    }

    @Override
    public void delete(long userId, long meetingId) {

    }
}
