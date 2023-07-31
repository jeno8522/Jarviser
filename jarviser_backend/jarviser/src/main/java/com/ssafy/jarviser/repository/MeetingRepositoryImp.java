package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MeetingRepositoryImp implements MeetingRepository{

    @PersistenceContext
    private EntityManager em;


    @Override
    public long insertMeeting(Meeting meeting) {
        em.persist(meeting);
        return meeting.getId();
    }

    @Override
    public Meeting getMeeting(long meetingId) {
        return em.createQuery(
                "SELECT m " +
                        "FROM Meeting m " +
                        "WHERE m.id = :meetingId "
        ,Meeting.class)
                .setParameter("meetingId",meetingId)
                .getSingleResult();
    }

    @Override
    public Report findMeetingReport(long meetingId) {
        return em.createQuery(
                "SELECT r " +
                        "FROM Report r " +
                        "WHERE r.id = :meetingId ",Report.class
        )
                .setParameter("meetingId",meetingId)
                .getSingleResult();
    }

    @Override
    public List<Meeting> findAllMeetingByUserId(long userid) {
        return em.createQuery(
                        "SELECT m FROM Meeting m " +
                                "JOIN m.participants p " +
                                "JOIN p.user u " +
                                "WHERE u.id = :userid", Meeting.class)
                .setParameter("userid", userid)
                .getResultList();
    }
}
