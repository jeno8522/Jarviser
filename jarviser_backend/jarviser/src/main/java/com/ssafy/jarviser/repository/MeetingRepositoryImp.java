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
    public List<Meeting> findMeetingByUserUid(String uid) {
        return em.createQuery(
                        "SELECT m FROM Meeting m " +
                                "JOIN m.participants p " +
                                "JOIN p.user u " +
                                "WHERE u.uid = :uid", Meeting.class)
                .setParameter("uid", uid)
                .getResultList();
    }
}
