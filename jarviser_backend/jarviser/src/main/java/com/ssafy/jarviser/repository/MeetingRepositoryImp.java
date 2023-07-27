package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Meeting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MeetingRepositoryImp implements MeetingRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Meeting> findMeetingByUserEmail(String email) {
        return em.createQuery(
                        "SELECT m FROM Meeting m " +
                                "JOIN m.participants p " +
                                "JOIN p.user u " +
                                "WHERE u.email = :email", Meeting.class)
                .setParameter("email", email)
                .getResultList();
    }
}
