package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.AudioMessage;

import java.util.List;
import java.util.Map;

public interface KeywordService {
    Map<String,Double> staticsOfKeywords(List<AudioMessage> audioMessages, List<String> keywords);
}
