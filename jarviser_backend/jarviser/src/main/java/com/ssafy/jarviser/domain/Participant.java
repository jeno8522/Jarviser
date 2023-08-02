package com.ssafy.jarviser.domain;

import jakarta.persistence.*;
import jakarta.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private long id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ParticipantRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id" , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Meeting meeting;

    // N : M 연관 테이블의 양방향 맵핑 설정
    public static Participant participate(User user, Meeting meeting){
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setMeeting(meeting);
        participant.setStartTime(LocalDateTime.now());

        meeting.getParticipants().add(participant);
        return participant;
    }
}
