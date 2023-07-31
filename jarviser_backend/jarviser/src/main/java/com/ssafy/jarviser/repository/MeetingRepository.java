package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository{
    //미팅 레포트
    Report findMeetingReport(long meetingId);
    List<Meeting> findAllMeetingByUserId(long userid);//회원아이디를 통해 참여한 미팅 내역 출력

}
