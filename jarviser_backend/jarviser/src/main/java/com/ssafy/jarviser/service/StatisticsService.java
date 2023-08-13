package com.ssafy.jarviser.service;

import com.ssafy.jarviser.domain.Meeting;
import com.ssafy.jarviser.domain.Report;
import com.ssafy.jarviser.dto.TempTranscriptRecord;
import com.ssafy.jarviser.repository.MeetingRepository;
import com.ssafy.jarviser.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {
    private final HashMap<Long, TempTranscriptRecord> tempTranscriptHolder = new HashMap<>();
    private final OpenAIService openAIService;
    private final ReportRepository reportRepository;
    private final MeetingRepository meetingRepository;

    private final String partQuery = "이거 3줄 정도로 요약해줘";
    private final String wholeQuery = "이거 10줄 정도로 요약해줘";

    public List<StringBuilder> getAccumulatedTranscriptById(Long meetingId){
        return tempTranscriptHolder.get(meetingId).getPartScripts();
    }
    @Async      // 비동기 처리
    public void accumulateTranscript(Long meetingId, String text){
        if (!tempTranscriptHolder.containsKey(meetingId)) {
            TempTranscriptRecord tempTranscriptRecord = new TempTranscriptRecord();
            tempTranscriptRecord.append(text);
            tempTranscriptHolder.put(meetingId, tempTranscriptRecord);
        }else{
            tempTranscriptHolder.get(meetingId).append(text);
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Void> summarizeTranscript(Long meetingId) {
        TempTranscriptRecord tempTranscriptRecord = tempTranscriptHolder.get(meetingId);
        ArrayList<StringBuilder> partScripts = tempTranscriptRecord.getPartScripts();
        List<CompletableFuture<String>> futurePartSummaries = new ArrayList<>();

        int idx = 0;
        for (StringBuilder partScript : partScripts) {
            String part = partScript.toString();
            log.info("부분요약 요청 전" + idx + "{}", part);
            CompletableFuture<String> futureSummary = openAIService.chatGPTSummary(part, partQuery)
                    .toFuture()
                    .orTimeout(60, TimeUnit.SECONDS);

            futureSummary.thenRun(() -> log.info("Task completed: " )); // Task completion log
            futureSummary.whenComplete((result, ex) -> { // Logging completion status
                if (ex != null) {
                    log.error("Task " + idx + " failed with exception: " + ex.getMessage());
                } else {
                    log.info("Task " + idx + " completed with result: " + result);
                }
            });

            log.info("부분요약 요청 후" + idx + "{}", part);
            futurePartSummaries.add(futureSummary);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futurePartSummaries.toArray(new CompletableFuture[0]));
        allFutures.thenRun(() -> log.info("------------모든 비동기 작업 완료--------------"));

        StringBuilder summaryConquer = new StringBuilder();

        // 모든 작업이 완료되면 결과를 tempTranscriptRecord에 추가
        allFutures.thenCompose(v -> {
            log.info("------------compose 완료--------------");
            for (CompletableFuture<String> futurePartSummary : futurePartSummaries) {
                summaryConquer.append(futurePartSummary.join()); // 결과 가져오기
            }
            return CompletableFuture.supplyAsync(() ->
                    openAIService.chatGPTSummary(summaryConquer.toString(), wholeQuery).toFuture().join());
        }).exceptionally(ex -> {
            log.error("Error occurred in CompletableFuture chain: {}", ex.getMessage());
            return null; // 예외 처리 후 반환값 설정
        }).thenAccept(wholeSummary -> {
            if (wholeSummary != null) {
                log.info("전체요약: {}", wholeSummary);
                Meeting meeting = meetingRepository.findMeetingById(meetingId);
                Report report = Report.builder()
                        .summary(wholeSummary)
                        .meeting(meeting)
                        .build();
                reportRepository.save(report);
            }
        });

        tempTranscriptHolder.remove(meetingId); // 메모리 관리
        return allFutures;
    }
}