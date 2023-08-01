package com.ssafy.jarviser.domain;

import com.ssafy.jarviser.repository.MeetingRepository;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "sender")
    private String sender;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Meeting meeting;

    //양방향 맵핑
    public void setChatMessage(Meeting meeting){
        this.meeting = meeting;
        meeting.getChatMessages().add(this);
    }

}
