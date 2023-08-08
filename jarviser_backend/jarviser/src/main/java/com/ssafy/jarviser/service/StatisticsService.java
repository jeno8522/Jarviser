package com.ssafy.jarviser.service;

import com.ssafy.jarviser.dto.TempTranscriptRecordDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
@Slf4j
public class StatisticsService {
    private final HashMap<Long, TempTranscriptRecordDto> tempTranscriptHolder = new HashMap<>();

    public void accumulateTranscript(Long meetingId, String text){
        if (!tempTranscriptHolder.containsKey(meetingId)) {
            TempTranscriptRecordDto tempTranscriptRecordDto = new TempTranscriptRecordDto();
            tempTranscriptRecordDto.append(text);
            tempTranscriptHolder.put(meetingId, tempTranscriptRecordDto);
        }else{
            tempTranscriptHolder.get(meetingId).getPartSummaries().add(new StringBuilder(text));
        }
    }
}
