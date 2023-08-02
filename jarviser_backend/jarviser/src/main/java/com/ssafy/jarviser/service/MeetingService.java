package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.ResponseMeetingStatics;

import java.util.List;

public interface MeetingService{
    //미팅 생성
    void createMeeting(User host, Meeting meeting);
    //참여자 등록
    void joinMeeting(User user, Meeting meeting);

    //미팅 조회
    Meeting findMeetingById(long meetingId);
    //미팅 참여자 조회
    List<User> findUserListByMeetingId(long meetingId);

    //미팅 통계 상세보기
    ResponseMeetingStatics meetingStatics(long meetingId);

    //미팅 참여내역 보기
    List<Meeting> findMeetingListByUserId(long userid);

    //리포트 열람
    Report meetingReport(long meetingId);

}