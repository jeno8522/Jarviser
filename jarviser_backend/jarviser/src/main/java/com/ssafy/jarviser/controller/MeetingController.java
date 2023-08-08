package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.dto.RequestJoinMeetingDto;
import com.ssafy.jarviser.security.JwtService;
import com.ssafy.jarviser.service.MeetingService;
import com.ssafy.jarviser.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("meeting")
public class MeetingController {
    private final JwtService jwtService;
    private final OpenAIService openAIService;
    private final MeetingService meetingService;

    @PostMapping(value = "/transcript", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> transcript(@RequestParam("file") MultipartFile file) throws IOException {
        HttpStatus status = null;
        String filePath = "audio/" + file.getOriginalFilename();
        log.debug(filePath);
        String textResponse = "failed";

        try (
                FileOutputStream fos = new FileOutputStream(filePath);
                // 파일 저장할 경로 + 파일명을 파라미터로 넣고 fileOutputStream 객체 생성하고
                InputStream is = file.getInputStream();) {

            int readCount = 0;
            byte[] buffer = new byte[1024];

            while ((readCount = is.read(buffer)) != -1) {
                //  파일에서 가져온 fileInputStream을 설정한 크기 (1024byte) 만큼 읽고

                fos.write(buffer, 0, readCount);
                // 위에서 생성한 fileOutputStream 객체에 출력하기를 반복한다
            }
        } catch (Exception ex) {
            throw new RuntimeException("file Save Error");
        }
        try {
            textResponse = openAIService.whisperAPICall(filePath).block();
            log.debug("textResponse : {}", textResponse);
        } catch (Exception e) {
            log.error("텍스트 보내기 실패 : {}", e);
        }

        return new ResponseEntity<>(textResponse, status.OK);
    }


    //미팅생성
    @PostMapping("/create/{meetingName}")
    public ResponseEntity<Map<String ,Object>> createMeeting(
            @RequestHeader("Authorization") String token,
            @RequestParam String meetingName){
        log.debug("CreateMeeting............................create meetingName:" + meetingName);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        token = token.split(" ")[1];
        try {
            Long hostId = jwtService.extractUserId(token);
            meetingService.createMeeting(hostId,meetingName);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("미팅 생성 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    //미팅 참여
    @PostMapping("/joinMeeting/{meetingId}")
    public ResponseEntity<Map<String,Object>> joinMeeting(
            @RequestHeader("Authorization") String token,
            @PathVariable Long meetingId) {

        Meeting meeting = meetingService.findMeetingById(meetingId);
        log.debug("JoinMeeting............................Join meetingName:" + meeting.getMeetingName());

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            Long joinUserId = jwtService.extractUserId(token);
            meetingService.joinMeeting(joinUserId, meeting);
            resultMap.put("meeting", meeting);
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            log.error("미팅 참여 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }
    //미팅 조회
    //미팅 참여자 조회
    //미팅 통계 상세보기
    //사용자 미팅 참여내역 보기
    //리포트 열람
}
