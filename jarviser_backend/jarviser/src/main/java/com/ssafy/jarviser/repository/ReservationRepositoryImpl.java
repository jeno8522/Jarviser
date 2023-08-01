package com.ssafy.jarviser.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.jarviser.domain.QReservation;
import com.ssafy.jarviser.domain.QUser;
import com.ssafy.jarviser.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<User> getUsersForMeetingRoom (Long id) {
        QReservation reservation = QReservation.reservation;
        QUser user = QUser.user;

        return queryFactory
                .select(user)
                .from(reservation)
                .join(reservation.user, user)
                .where(reservation.reservatedMeeting.id.eq(id))
                .fetch();
    }
}
