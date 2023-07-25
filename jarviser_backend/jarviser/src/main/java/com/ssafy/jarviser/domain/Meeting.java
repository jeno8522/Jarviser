package com.ssafy.jarviser.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private long id;

    @Column(name = "meeting_name")
    private String meetingName;

    @Column(name = "host_id")
    private long hostId;

    @Column(name = "meeting_url")
    private String meetingUrl;

    @Column(name = "meeting_status")
    @Enumerated(EnumType.STRING)
    private MeetingStatus meetingStatus; //미팅상태 [NOW,RES]

    @OneToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @OneToMany(mappedBy = "meeting")
    List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    List<AudioMessage> audioMessages = new ArrayList<>();
}
