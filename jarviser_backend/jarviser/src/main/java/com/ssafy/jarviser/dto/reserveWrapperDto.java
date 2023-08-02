package com.ssafy.jarviser.dto;

import lombok.Data;

import java.util.List;

@Data
public class reserveWrapperDto {
    private ReservatedMeetingDto reservatedMeetingDto;
    List<String> emails;
}
