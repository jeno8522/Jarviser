package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository{
    List<Meeting> findMeetingByUserEmail(String email); //회원이메일 통해 참여한 미팅 내역 출력
}
