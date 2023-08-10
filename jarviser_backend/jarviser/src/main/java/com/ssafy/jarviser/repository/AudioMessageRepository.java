package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.AudioMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioMessageRepository extends JpaRepository<AudioMessage,Long> {
}
