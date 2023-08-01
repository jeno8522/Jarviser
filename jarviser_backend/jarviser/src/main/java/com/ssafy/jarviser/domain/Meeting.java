package com.ssafy.jarviser.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
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

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @OneToMany(mappedBy = "meeting")
    private List<AudioMessage> audioMessages = new ArrayList<>();

    @OneToMany(mappedBy = "meeting")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    @OneToOne(mappedBy = "meeting")
    private Report report;

    @OneToMany(mappedBy = "meeting")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Meeting(long id,String meetingName,long hostId,String meetingUrl,LocalDateTime startTime){
        this.id = id;
        this.meetingName = meetingName;
        this.hostId = hostId;
        this.meetingUrl = meetingUrl;
        this.startTime = startTime;
    }

    public void setReport(Report report){
        this.report = report;
    }
}
