package com.ssafy.jarviser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TempTranscriptRecordDto {
    private String wholeSummary;
    private ArrayList<StringBuilder> partSummaries;
    private int partIndex;

    public void append(String part){
        if (partSummaries == null){
            partSummaries = new ArrayList<>();
            partSummaries.add(new StringBuilder(part));
            partIndex = 1;
            return;
        }
        if (partSummaries.get(partIndex).length() < 2000){
            partSummaries.get(partIndex).append(part);
        }else{
            partSummaries.add(new StringBuilder(part));
            partIndex++;
        }
    }
}
