package com.ssafy.jarviser.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private int id;

    private String userId;

    private String password;

    private String name;

    private String email;

    private String provider;

    private String providedId;

    @Builder
    public User(int id, String userId, String password, String name, String email
            , String provider, String providedId) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providedId = providedId;
    }

}