package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.AudioMessage;
import com.ssafy.jarviser.dto.ResponseChatGPTKeywordsDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import java.util.*;

@Service
@Slf4j
@PropertySource("classpath:appKey.yml")
public class OpenAIService {

    @Value("${key}")
    private String token;

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

    public List<String> chatGTPKeywords(List<AudioMessage> audioMessages) {
        WebClient webClient = WebClient.create("https://api.openai.com/v1/chat/completions");
        String model = "gpt-3.5-turbo";
        StringBuilder content = new StringBuilder();

        for (AudioMessage audioMessage : audioMessages) {
            content.append(audioMessage.getContent()).append(' ');
        }
        String contentString = content.toString();

        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", Arrays.asList(
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", contentString + " 위 내용의 키워드를 문자열로 반환해줘");
                }}
        ));

        String resultString = webClient.post()
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(ResponseChatGPTKeywordsDTO.class)
                .map(response -> response.getChoices().get(0).getMessage().getContent())
                .doOnError(e -> {
                    // Log error or take action
                    System.out.println("Error occurred: " + e.getMessage());
                })
                .block();

        assert resultString != null;
        String[] split = resultString.split(", ");
        return Arrays.asList(split);

    }
}
