package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.dto.ResponseMeetingStatics;

import java.util.List;

public interface MeetingService {
    //미팅 통계 상세보기
    ResponseMeetingStatics meetingStatics(long meetingId);
    //미팅 참여내역 보기
    List<Meeting> meetingList(long userid);
    //리포트 열람
    Report meetingReport(long userId,long meetingId);
    //리포트 수정
    void update(long userId,long meetingId);
    //리포트 삭제
    void delete(long userId,long meetingId);
}