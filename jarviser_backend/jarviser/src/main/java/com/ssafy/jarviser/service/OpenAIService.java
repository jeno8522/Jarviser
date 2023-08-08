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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class OpenAIService {

    private static final String token = "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";
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

    public Mono<String> chatGPTPartSummary(String filePath){
        WebClient webClient = WebClient.create("https://api.openai.com/v1/chat/completions");
        String model = "gpt-3.5-turbo";

        // Read the file content
        String fileContent;
        try {
            fileContent = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", Arrays.asList(
                new HashMap<String, String>() {{ put("role", "user"); put("content", fileContent + "이거 짧게 한글로 요약해줘"); }}
        ));

        System.out.println(fileContent);
        return
                webClient.post()
                        .header("Authorization", "Bearer " + token) // Replace "YOUR_OPENAI_API_KEY" with your actual OpenAI API key.
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(requestBody))
                        .retrieve()
                        .bodyToMono(String.class)
                        // Assuming the result is of type String. Modify this part as appropriate based on the actual response type.
                        .doOnError(e -> {
                            // Log error or take action
                            System.out.println("Error occurred: " + e.getMessage());
                        });
    }
}
