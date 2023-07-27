package com.ssafy.jarviser.controller;

import com.ssafy.jarviser.dto.AudioDto;
import com.ssafy.jarviser.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
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
    private final OpenAIService openAIService;

    @PostMapping(value = "/transcript", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> transcript(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, String> resultMap = new HashMap<>();
        HttpStatus status = null;
        String filePath = "audio/" + file.getOriginalFilename();
        log.debug(filePath);

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
            resultMap.put("text", textResponse);
        } catch (Exception e) {
            log.error("텍스트 보내기 실패 : {}", e);
        }

        return new ResponseEntity<>(resultMap, status.OK);
    }
}
