package com.ssafy.jarviser.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private long id;

    @Column(name = "summary")
    private String summary;

    @OneToOne(mappedBy = "report")
    private Meeting meeting;
}
