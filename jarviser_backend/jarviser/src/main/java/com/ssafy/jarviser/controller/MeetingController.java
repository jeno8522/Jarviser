package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.domain.AudioMessage;
import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.dto.RequestMeetingIdDto;
import com.ssafy.jarviser.dto.ResponseAudioMessage;
import com.ssafy.jarviser.security.JwtService;
import com.ssafy.jarviser.service.AudioService;
import com.ssafy.jarviser.service.KeywordService;
import com.ssafy.jarviser.service.MeetingService;
import com.ssafy.jarviser.service.OpenAIService;
import com.ssafy.jarviser.util.AESEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"*"})
@RequestMapping("meeting")
public class MeetingController {
    private final JwtService jwtService;
    private final OpenAIService openAIService;
    private final MeetingService meetingService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AESEncryptionUtil aesEncryptionUtil;
    private final AudioService audioService;
    private final KeywordService keywordService;

    @PostMapping(value = "/transcript", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> transcript(@RequestParam("file") MultipartFile file, Long meetingId) throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        String filePath = "audio/" + file.getOriginalFilename();
        log.debug(filePath);

        //TODO: 추후 MultiPartFile을 File로 즉각 변환해본 후 성능 테스트해보기
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
            String textResponse = openAIService.whisperAPICall(filePath).block();
            assert textResponse != null;
            messagingTemplate.convertAndSend("/topic/meeting/" + meetingId, textResponse);
            resultMap.put("text", textResponse);
        } catch (Exception e) {
            log.error("텍스트 보내기 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status.OK);
    }


    //미팅생성
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createMeeting(
            @RequestHeader("Authorization") String token,
            @RequestBody String meetingName) {
        log.debug("CreateMeeting............................create meetingName:" + meetingName);

        Map<String, Object> responseMap = new HashMap<>();
        HttpStatus httpStatus = null;
        token = token.split(" ")[1];
        try {
            Long hostId = jwtService.extractUserId(token);
            Meeting meeting = meetingService.createMeeting(hostId, meetingName);
            String encryptedKey = meeting.getEncryptedKey();
            httpStatus = HttpStatus.ACCEPTED;
            responseMap.put("encryptedKey", encryptedKey);

        } catch (Exception e) {
            log.error("미팅 생성 실패 : {}", e);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(responseMap, httpStatus);
    }

    //미팅 참여
    @PostMapping("/joinMeeting")
    public ResponseEntity<Map<String, Object>> joinMeeting(
            @RequestHeader("Authorization") String token,
            @RequestBody String encryptedKey) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            long meetingId = Long.parseLong(aesEncryptionUtil.decrypt(encryptedKey));
            Meeting meeting = meetingService.findMeetingById(meetingId);
            log.debug("JoinMeeting............................Join meetingName:" + meeting.getMeetingName());
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

    //미팅 오디오 메시지 불러오는 api
    @GetMapping("/audiomessage")
    public ResponseEntity<Map<String, Object>> meetingDetail(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestMeetingIdDto requestMeetingIdDto
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        try {

            Meeting meeting = meetingService.findMeetingById(requestMeetingIdDto.getMeetingId());
            List<AudioMessage> audioMessages = meeting.getAudioMessages();
            List<ResponseAudioMessage> responseAudioMessages = new ArrayList<>();

            for (AudioMessage audioMessage : audioMessages) {
                responseAudioMessages.add(new ResponseAudioMessage(audioMessage.getUserName(), audioMessage.getContent(), audioMessage.getSpeechLength()));
            }
            response.put("audioMessages", responseAudioMessages);
            httpStatus = HttpStatus.OK;

        } catch (Exception e) {
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    //미팅 발화자들 마다 발화 비율 api
    @GetMapping("/speech")
    public ResponseEntity<Map<String, Object>> meetingSpeech(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestMeetingIdDto requestMeetingIdDto
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            List<AudioMessage> audioMessages = meetingService.findAudioMessageByMeetingId(requestMeetingIdDto.getMeetingId());
            Map<String, Double> speechPercentage = audioService.staticsOfAudioMessages(audioMessages);
            response.put("speechPercentage", speechPercentage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    //미팅 키워드 비율 추출
    @GetMapping("/keywords")
    public ResponseEntity<Map<String, Object>> meetingKeywords(
            @RequestHeader("Authorization") String token,
            @RequestBody RequestMeetingIdDto requestMeetingIdDto
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            List<AudioMessage> audioMessages = meetingService.findAudioMessageByMeetingId(requestMeetingIdDto.getMeetingId());
            List<String> keywords = openAIService.chatGTPKeywords(audioMessages);
            Map<String, Double> staticsOfKeywords = keywordService.staticsOfKeywords(audioMessages, keywords);
            response.put("staticsOfKeywords",staticsOfKeywords);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
