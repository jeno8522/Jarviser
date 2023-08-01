package com.ssafy.jarviser.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ReservatedMeeting reservatedMeeting;
}
