package com.ssafy.jarviser.domain;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "audio_message")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"meeting"})
public class AudioMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audio_message_id")
    private long id;

    @Column(name = "content")
    private String content;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "speech_length")
    private int speechLength;

    @Column(name = "index")
    private long index;

    @Column(name = "user_id")
    private long userId; //FIXME: 추후 many to one으로 FK 처리 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id" , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Meeting meeting;

}
