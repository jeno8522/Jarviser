package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.AudioMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioMessageRepository extends JpaRepository<AudioMessage,Long> {
    List<AudioMessage> findAllByMeetingId(long meetingId);
}