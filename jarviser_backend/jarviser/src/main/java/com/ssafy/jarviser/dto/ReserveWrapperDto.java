package com.ssafy.jarviser.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReserveWrapperDto {
    private ReservatedMeetingDto reservatedMeetingDto;
    List<String> emails;
}
