package com.ssafy.jarviser.service;

import com.ssafy.jarviser.exception.ClientException;
import com.ssafy.jarviser.exception.ServerException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface AudioService {
    public Long getTimeOfAudio(MultipartFile audioFile) throws ClientException;
    public String saveAudioFile(String mId, long userId, long startTime, MultipartFile audioFile) throws ServerException;
    public String getStt(String filePath) throws ServerException;
    public Long createAudioMessage(Long userId, String mId, Long StartTime, String filePath, String stt) throws ServerException;
}
