package com.ssafy.jarviser.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String uid;

    private String password;

    private String name;

    private String email;

    @OneToMany(mappedBy = "user")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public User(long id, String uid, String password, String name, String email) {
        this.id = id;
        this.uid = uid;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
