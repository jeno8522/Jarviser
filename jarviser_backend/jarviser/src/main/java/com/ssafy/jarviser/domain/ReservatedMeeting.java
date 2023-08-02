package com.ssafy.jarviser.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "reservated_meeting")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"reservations"})
public class ReservatedMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservated_meeting_id")
    private long id;

    @Column(name = "meeting_name")
    private String meetingName;

    @Column(name = "host_id")
    private long hostId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "reservatedMeeting")
    private List<Reservation> reservations = new ArrayList<>();

    public List<Reservation> getReservations() {
        if (reservations == null) {
            reservations = new ArrayList<>();
        }
        return reservations;
    }
}
