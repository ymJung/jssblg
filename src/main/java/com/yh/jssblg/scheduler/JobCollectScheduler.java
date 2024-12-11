package com.yh.jssblg.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yh.jssblg.service.JSSService;

import lombok.RequiredArgsConstructor;

/**
 * TODO:
 *  1. @Scheduled 애노테이션으로 일정 주기마다 collectJobs 호출
 *  2. 크론 표현식 사용
 */
@Component
@RequiredArgsConstructor
public class JobCollectScheduler {

    private final JSSService jssService;


    @Scheduled(cron = "0 0 * * * ?") 
    public void scheduledJobCollect() {
        // 매시 정각에 데이터 수집
        jssService.collectJobs();
    }
}
