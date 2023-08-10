package com.ssafy.jarviser.service;

import com.nimbusds.jose.shaded.gson.Gson;
import com.ssafy.jarviser.domain.AudioMessage;
import com.ssafy.jarviser.exception.ClientException;
import com.ssafy.jarviser.exception.ServerException;
import com.ssafy.jarviser.repository.AudioMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AudioServiceImp implements AudioService{
    private final OpenAIService openAIService;
    private final MeetingService meetingService;
    private final AudioMessageRepository audioMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final Gson gson = new Gson();


    @Override
    public Long getTimeOfAudio(MultipartFile audioFile) {
        try {
            // MultipartFile을 InputStream으로 변환
            InputStream inputStream = audioFile.getInputStream();

            // InputStream을 AudioInputStream으로 변환
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);

            // AudioFileFormat 가져오기
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(audioInputStream);

            // 프레임 크기와 프레임 레이트 가져오기
            int frameSize = fileFormat.getFormat().getFrameSize();
            float frameRate = fileFormat.getFormat().getFrameRate();

            // 파일 크기 가져오기
            long audioFileLength = audioFile.getSize();

            // 밀리초 단위의 시간 계산
            float durationInSeconds = (audioFileLength / (frameSize * frameRate));
            long durationInMilliseconds = (long) (durationInSeconds * 1000);

            return durationInMilliseconds;
        } catch (Exception e) {
            log.error("get time of audio error", e);
            throw new ClientException("get time of audio error");
        }
    }

    @Override
    public String saveAudioFile(String mId, long userId, long startTime, MultipartFile audioFile){
        String filePath = "audio/"+mId+"/"+userId+"/"+startTime+".wav"; //TODO: .wav 파일을 하드코딩한 부분에 대한 고려 필요
        try{
            File savedFile = new File(filePath);
            if(!savedFile.getParentFile().exists()){
                savedFile.getParentFile().mkdirs();
            }
            audioFile.transferTo(savedFile);
            return filePath;
        }catch (Exception e) {
            log.error("save audio error", e);
            throw new ServerException("save audio error");
        }
    }

    @Override
    public String getStt(String filePath){
        String stt;
        try{
            String textResponse = openAIService.whisperAPICall(filePath).block();
            stt = (String) gson.fromJson(textResponse, HashMap.class).get("text");
        }catch (Exception e){
            log.error("get stt error", e);
            throw new ServerException("get stt error");
        }
        //TODO: 이상한 값이 오는 것 같으면 stt를 ""로 바꿔주는 로직 필요
        return stt;
    }

    @Override
    @Transactional
    public Long createAudioMessage(Long userId, String mId, Long StartTime, String filePath, String stt){
        try{
            AudioMessage audioMessage = AudioMessage.builder()
                    .userId(userId)
                    .meeting(meetingService.findMeetingById(Long.parseLong(mId)))
                    .startTime(new Date(StartTime))
                    .filePath(filePath)
                    .content(stt)
                    .build();
            audioMessageRepository.save(audioMessage);
            return audioMessage.getId();
        }catch (Exception e){
            log.error("create audio message error", e);
            throw new ServerException("create audio message error");
        }
    }



}
