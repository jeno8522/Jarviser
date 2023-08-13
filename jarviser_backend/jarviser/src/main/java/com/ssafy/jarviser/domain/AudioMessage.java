package com.ssafy.jarviser.domain;

import java.text.DateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm", timezone="Asia/Seoul")
    @CreationTimestamp
    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "speech_length")
    private int speechLength;

    @Column(name = "priority")
    private long priority;

    @Column(name = "user_id")
    private long userId; //FIXME: 추후 many to one으로 FK 처리 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id" , foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Meeting meeting;

}
