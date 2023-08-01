package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.Participant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantRepositoryImp implements ParticipantRepository{
    @PersistenceContext
    private EntityManager em;
    @Override
    public void joinParticipant(Participant participant) {
        em.persist(participant);
    }
}
