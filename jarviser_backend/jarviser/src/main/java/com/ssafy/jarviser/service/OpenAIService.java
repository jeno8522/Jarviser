package com.ssafy.jarviser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@Slf4j
public class OpenAIService {
    public Mono<String> whisperAPICall(String filePath) throws URISyntaxException, IOException {

        WebClient webClient = WebClient.create("https://api.openai.com/v1/audio/transcriptions");

        // 요청에 필요한 데이터 및 파일 경로 설정
        String model = "whisper-1";
        String token = "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"; // API 인증 토큰

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(filePath), MediaType.APPLICATION_OCTET_STREAM);
        builder.part("model", model);

        return
        webClient.post()
                .header("Authorization", "Bearer " + token) // "YOUR_OPENAI_API_KEY"를 실제 OpenAI API 키로 대체하세요.
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class) // 결과가 String 타입이라고 가정합니다. 실제 응답 유형에 따라 이 부분을 적절히 수정하세요.
                .doOnError(e -> {
                    // Log error or take action
                    System.out.println("Error occurred: " + e.getMessage());
                });
    }
}
