package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.AudioMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AudioServiceImp implements AudioService{
    @Override
    public Map<String, Double> staticsOfAudioMessages(List<AudioMessage> audioMessages) {
        Map<String,Integer> countOfStatic = new HashMap<>();
        Map<String,Double> staticOfAudioMessages = new HashMap<>();

        int totalLength = 0;

        for(AudioMessage audioMessage : audioMessages){
            String name = audioMessage.getUserName();
            int length = audioMessage.getSpeechLength();
            totalLength += length;

            if(!countOfStatic.containsKey(name)){
                countOfStatic.put(name,length);
            }else{
                int prevLength = countOfStatic.get(name);
                countOfStatic.put(name,length + prevLength);
            }
        }

        //getKeys
        Set<String> names = countOfStatic.keySet();
        for(String name : names){
            double percentage = (double) countOfStatic.get(name) * 100 / totalLength;
            staticOfAudioMessages.put(name,percentage);
        }
        return staticOfAudioMessages;
    }
}
