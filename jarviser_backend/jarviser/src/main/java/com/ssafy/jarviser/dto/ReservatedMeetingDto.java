package com.ssafy.jarviser.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservatedMeetingDto {
    private String meetingName;
    private LocalDateTime startTime;
    private String description;
}
