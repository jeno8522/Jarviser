package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.AudioMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class OpenAIServiceTest {

    @Autowired
    OpenAIService openAIService;

    @Autowired
    AudioService audioService;

    @Autowired
    MeetingService meetingService;

    @Autowired
    KeywordService keywordService;

//    @Test
//    @DisplayName("회원 가입 테스팅")
//    void testChatGPT() throws URISyntaxException, IOException {
//        String text = openAIService.chatGPTPartSummary("C:\\Users\\SSAFY\\IdeaProjects\\S09P12A506\\text\\hi.txt").block();
//        System.out.println(text);
//    }


    @Test
    @DisplayName("keywordTesting")
    void testKeywordExtraction() throws URISyntaxException, IOException {
        List<AudioMessage> audioMessages = meetingService.findAudioMessageByMeetingId(1);
        List<String> keywords = openAIService.chatGTPKeywords(audioMessages);
        Map<String, Double> stringDoubleMap = keywordService.staticsOfKeywords(audioMessages, keywords);

    }

}