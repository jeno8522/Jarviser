package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.domain.User;

import java.util.List;

public interface MeetingRepository{
    //미팅 등록
    void saveMeeting(Meeting meeting);
    //미팅 조회
    Meeting findMeetingById(long meetingId);

    //회원아이디를 통해 참여한 미팅 내역 출력
    List<Meeting> findAllMeetingByUserId(long userid);

    //미팅 아이디를 통해 참여했던 회원리스트 반환
    List<User> findUserListByMeetingId(long meetingId);

    //미팅의 리포트 겨저오기
    Report findMeetingReportByMeetingId(long meetingId);
}