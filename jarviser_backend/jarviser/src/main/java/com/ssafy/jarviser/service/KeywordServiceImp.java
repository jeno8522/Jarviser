package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.AudioMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordServiceImp implements KeywordService{

    @Override
    public Map<String, Double> staticsOfKeywords(List<AudioMessage> audioMessages, List<String> keywords) {
        int total = 0;
        Map<String,Integer> cntKeywords = new HashMap<>();
        Map<String,Double> staticKeywords = new HashMap<>();

        for(String keyword : keywords){
            int keywordCnt = 0;
            for(AudioMessage audioMessage : audioMessages){
                if(audioMessage.getContent().contains(keyword)){
                    keywordCnt++;
                    total++;
                }
            }
            cntKeywords.put(keyword,keywordCnt);
        }

        System.out.println("총 발화 카운트");
        System.out.println(total);
        System.out.println("=====================");

        for(String keyword : keywords){
            staticKeywords.put(keyword, (double) (cntKeywords.get(keyword)  * 100/ total));
        }

        return staticKeywords;
    }
}
