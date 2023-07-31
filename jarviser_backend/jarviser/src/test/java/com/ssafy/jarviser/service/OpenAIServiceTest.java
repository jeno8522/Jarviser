package com.ssafy.jarviser.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootTest
class OpenAIServiceTest {

    @Autowired
    OpenAIService openAIService;

    @Test
    @DisplayName("회원 가입 테스팅")
    void testChatGPT() throws URISyntaxException, IOException {
        String text = openAIService.chatGPTPartSummary("C:\\Users\\SSAFY\\IdeaProjects\\S09P12A506\\text\\hi.txt").block();
        System.out.println(text);
    }

}