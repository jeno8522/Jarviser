package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.User;

import java.util.List;

public interface ReservationRepositoryCustom {
    List<User> getUsersForMeetingRoom (Long id);
}
