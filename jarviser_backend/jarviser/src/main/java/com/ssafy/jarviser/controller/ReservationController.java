package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.domain.ReservatedMeeting;
import com.ssafy.jarviser.dto.ReservatedMeetingDto;
import com.ssafy.jarviser.dto.reserveWrapperDto;
import com.ssafy.jarviser.security.JwtService;
import com.ssafy.jarviser.service.ReservationService;
import com.ssafy.jarviser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("reservation")
public class ReservationController {

    private final JwtService jwtService;
    private final ReservationService reservationService;
    private final UserService userService;


    @PostMapping("")
    public ResponseEntity<Map<String,Object>> reservate(
            @RequestHeader("Authorization") String token,
            @RequestBody reserveWrapperDto reserveWrapperDto
    ){
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try{
            List<String> emails = reserveWrapperDto.getEmails();
            ReservatedMeetingDto reservatatedMeetingDto = reserveWrapperDto.getReservatedMeetingDto();
            Long hostId = jwtService.extractUserId(token);

            ReservatedMeeting reservatedMeeting = ReservatedMeeting.builder()
                    .meetingName(reservatatedMeetingDto.getMeetingName())
                    .hostId(hostId)
                    .startTime(reservatatedMeetingDto.getStartTime())
                    .description(reservatatedMeetingDto.getDescription())
                    .build();

            reservationService.createReservation(reservatedMeeting, emails);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        }catch (Exception e){
            log.error("예약 실패 : {}", e);
            resultMap.put("message", "fail");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
