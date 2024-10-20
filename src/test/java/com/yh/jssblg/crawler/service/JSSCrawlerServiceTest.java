package com.yh.jssblg.crawler.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yh.jssblg.crawler.model.RecruitJobDTO;

import lombok.extern.slf4j.Slf4j;


@SpringBootTest
@Slf4j
public class JSSCrawlerServiceTest {
    @Autowired
    private JSSCrawlerService jssCrawlerService;

    @Test
    void testScrapeAndFilterJobs() {
        List<RecruitJobDTO> recruitJobDTOs = jssCrawlerService.scrapeAndFilterJobs();
        log.info("recruitJobDTOs : {}", recruitJobDTOs);
        Assertions.assertFalse(recruitJobDTOs.isEmpty());
    }
}
