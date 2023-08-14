package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.domain.AudioMessage;
import com.ssafy.jarviser.domain.KeywordStatistics;
import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.dto.KeywordStatisticsDTO;
import com.ssafy.jarviser.dto.ParticipantsStaticsDTO;
import com.ssafy.jarviser.dto.ResponseAudioMessageDTO;
import com.ssafy.jarviser.security.JwtService;
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

    @PostMapping(value = "/transcript", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> transcript(@RequestParam("file") MultipartFile file, Long meetingId) throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        String filePath = "audio/" + file.getOriginalFilename();
        log.debug(filePath);

        /*
        TODO: 추후 MultiPartFile을 File로 즉각 변환해본 후 성능 테스트해보기
               비동기처리를 하고 싶을 때 파일 저장부분을 따로 분리해서 해당 함수 위에 @Async
               그리고 테스트를 해보고싶으면 해당 함수 내에서 Thread.sleep
         */
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
            String encryptedKey = meetingService.createMeeting(hostId, meetingName);
            httpStatus = HttpStatus.ACCEPTED;
            responseMap.put("encryptedKey", encryptedKey);
        } catch (Exception e) {
            log.error("미팅 생성 실패 : {}", e);
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(responseMap, httpStatus);
    }

    //미팅 참여
    @PostMapping("/joinMeeting/{encryptedKey}")
    public ResponseEntity<Map<String, Object>> joinMeeting(
            @RequestHeader("Authorization") String token,
            @PathVariable String encryptedKey) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;

        try {
            //미팅 복호화를 통해 미팅 id값 획득
            long meetingId = Long.parseLong(aesEncryptionUtil.decrypt(encryptedKey));
            //해당 미팅 id값을 통해 미팅 객체 찾기
            Meeting meeting = meetingService.findMeetingById(meetingId);
            //유저 id jwt토큰을 이용해서 획득
            Long joinUserId = jwtService.extractUserId(token);
            meetingService.joinMeeting(joinUserId, meetingId);
            resultMap.put("meeting", meeting);

            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }


    //미팅 오디오 메시지 불러오는 api
    @GetMapping("/audiomessage/{encryptedKey}")
    public ResponseEntity<Map<String, Object>> meetingAudioMessages(
            @RequestHeader("Authorization") String token,
            @PathVariable String encryptedKey
    ) {
        Map<String,Object> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        try {
            long meetingId = Long.parseLong(aesEncryptionUtil.decrypt(encryptedKey));
            List<AudioMessage> allAudioMessage = meetingService.findAllAudioMessage(meetingId);
            //DTO로 변환
            List<ResponseAudioMessageDTO> responseAudioMessageDTOList = new ArrayList<>();
            for(AudioMessage audioMessage : allAudioMessage){
                ResponseAudioMessageDTO responseAudioMessageDTO = ResponseAudioMessageDTO
                        .builder()
                        .length(audioMessage.getSpeechLength())
                        .priority(audioMessage.getPriority())
                        .content(audioMessage.getContent())
                        .startTime(audioMessage.getStartTime())
                        .name(audioMessage.getUser().getName())
                        .filePath(audioMessage.getFilePath())
                        .build();
                responseAudioMessageDTOList.add(responseAudioMessageDTO);
            }
            response.put("audioMessages",responseAudioMessageDTOList);

        } catch (Exception e) {
            httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    //미팅 발화자 통계 불러오는 api
    @GetMapping("/speech/{encryptedKey}")
    public ResponseEntity<Map<String, Object>> meetingSpeech(
            @RequestHeader("Authorization") String token,
            @PathVariable String encryptedKey
    ) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            //미팅 방 가져오고
            long meetingId = Long.parseLong(aesEncryptionUtil.decrypt(encryptedKey));
            List<ParticipantsStaticsDTO> participantStatistics = meetingService.caculateParticipantsStatics(meetingId);
            response.put("statistics",participantStatistics);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, httpStatus);
    }


    //미팅 키워드 통계 불러오는 api
    @GetMapping("/keywords/{encryptedKey}")
    public ResponseEntity<Map<String, Object>> meetingKeywords(
            @RequestHeader("Authorization") String token,
            @PathVariable String encryptedKey
    ) {

        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            long meetingId = Long.parseLong(aesEncryptionUtil.decrypt(encryptedKey));
            List<KeywordStatistics> allKeywordStatistics = meetingService.findAllKeywordStatistics(meetingId);
            List<KeywordStatisticsDTO> allKeywordStatisticsDTO = new ArrayList<>();

            for(int i = 0 ;i<allKeywordStatistics.size();i++){
                KeywordStatistics keywordStatistics = allKeywordStatistics.get(i);
                KeywordStatisticsDTO keywordStatisticsDTO = KeywordStatisticsDTO
                        .builder()
                        .keyword(keywordStatistics.getKeyword())
                        .percent(keywordStatistics.getPercent())
                        .build();
                allKeywordStatisticsDTO.add(keywordStatisticsDTO);
            }
            response.put("statistics",allKeywordStatisticsDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
