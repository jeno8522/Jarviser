package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.AudioMessage;

import java.util.Map;
import java.util.List;

public interface AudioService {
    Map<String,Double> staticsOfAudioMessages(List<AudioMessage> audioMessages);
}
