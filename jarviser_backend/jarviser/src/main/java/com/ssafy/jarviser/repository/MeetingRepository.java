package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository{
    List<Meeting> findMeetingByUserUid(String uid); //회원아이디를 통해 참여한 미팅 내역 출력
}
