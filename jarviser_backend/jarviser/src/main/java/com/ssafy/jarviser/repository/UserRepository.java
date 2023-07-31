package com.ssafy.jarviser.repository;

import com.ssafy.jarviser.domain.User;
import com.ssafy.jarviser.dto.RequestUpdateUserDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(long id);

    Optional<User> findByEmail(String email); //what is Optional?

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.password = :password, u.name = :name WHERE u.id = :id")
    void updateUser(@Param("id") long id, @Param("password") String password, @Param("name") String name);

}
