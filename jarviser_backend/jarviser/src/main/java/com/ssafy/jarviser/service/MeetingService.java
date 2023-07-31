package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Participant;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.dto.ResponseMeetingStatics;

import java.util.List;

public interface MeetingService{
    //미팅 등록
    long insertMeeting(Meeting meeting);

    //참여자 등록
    void joinMeeting(Participant participant);

    //미팅 조회
    Meeting getMeeting(long meetingId);

    //미팅 통계 상세보기
    ResponseMeetingStatics meetingStatics(long meetingId);

    //미팅 참여내역 보기
    List<Meeting> meetingList(long userid);

    //리포트 열람
    Report meetingReport(long userId,long meetingId);

    //리포트 삭제
    void delete(long userId,long meetingId);
}