package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.AudioMessage;
import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.ResponseAudioMessage;

import java.util.List;

public interface MeetingService{
    //미팅 생성
    Meeting createMeeting(Long hostId, String meetingName);
    //미팅 참여자 등록
    void joinMeeting(Long joinUserId, Meeting meeting);

    //미팅 아이디로 미팅 조회
    Meeting findMeetingById(long meetingId);
    //미팅 아이디로 미팅 참여자들 조회
    List<User> findUserListByMeetingId(long meetingId);

    //미팅 통계 상세보기
    Report findMeetingStaticsByMeetingId(long meetingId);

    //유저아이디로 미팅참여 보기
    List<Meeting> findMeetingListByUserId(long userid);

    //리포트 열람
    Report meetingReport(long meetingId);

    //발화 내용 보기
    List<AudioMessage> findAudioMessageByMeetingId(long meetingId);

    //오디오 메시지 미팅 저장
    void addAudioMessageToMeeting(long meetingId, AudioMessage audioMessage);


}