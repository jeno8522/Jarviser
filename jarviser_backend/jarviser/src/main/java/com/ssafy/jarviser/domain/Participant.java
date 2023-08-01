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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id" , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Meeting meeting;

    //참여자 미팅 등록
    public static Participant setParticipant(User user, Meeting meeting){
        Participant participant = new Participant();
        participant.setUser(user);
        participant.setMeeting(meeting);
        participant.setStartTime(LocalDateTime.now());
        meeting.getParticipants().add(participant);
        return participant;
    }
}
