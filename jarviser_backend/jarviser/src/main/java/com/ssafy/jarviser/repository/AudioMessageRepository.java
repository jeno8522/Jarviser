package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.AudioMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioMessageRepository extends JpaRepository<AudioMessage,Long> {
    List<AudioMessage> findAllByMeetingId(long meetingId);

    //TODO: order by priority 로 해당 회의에 대한 모든 오디오 메시지를 가져오는 쿼리 작성

    //TODO: 위에서 가져온 애들의 priority를 다시 배분해주는 쿼리 작성

}