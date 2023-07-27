package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(long id);

    Optional<User> findByEmail(String email); //what is Optional?
}
