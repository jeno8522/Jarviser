package com.ssafy.jarviser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OpenAIService {

    public Mono<String> whisperAPICall(String filePath) throws URISyntaxException, IOException {

        WebClient webClient = WebClient.create("https://api.openai.com/v1/audio/transcriptions");

        // 요청에 필요한 데이터 및 파일 경로 설정
        String model = "whisper-1";

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
    private static final String token = "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";

    // @Async를 해주지 않아도 WebClient를 사용하고 Mono를 반환하는 것 만으로 비동기
    public Mono<String> chatGPTSummary(String textContent, String query){
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMinutes(2)) // 2분 응답 타임아웃
                .keepAlive(false);

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        String model = "gpt-3.5-turbo";

        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", Arrays.asList(
                new HashMap<String, String>() {{ put("role", "user"); put("content", textContent + query); }}
        ));

        return
                webClient.post()
                        .header("Authorization", "Bearer " + token) // Replace "YOUR_OPENAI_API_KEY" with your actual OpenAI API key.
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(requestBody))
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(Duration.ofSeconds(60)) // 60초 타임아웃 설정
                        // Assuming the result is of type String. Modify this part as appropriate based on the actual response type.
                        .doOnError(e -> {
                            // Log error or take action
                            System.out.println("Error occurred: " + e.getMessage());
                        });
    }
}
