package com.yh.jssblg.crawler.controller;

import com.yh.jssblg.crawler.service.JSSCrawlerService;
import com.yh.jssblg.crawler.service.RecruitPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jss")
@RequiredArgsConstructor
@Slf4j
public class JSSController {

    private final JSSCrawlerService jssCrawlerService;
    private final RecruitPostService recruitPostService;

    @GetMapping("/crawl-and-save")
    public ResponseEntity<String> crawlAndSaveRecruitPosts() {
        try {
            int savedCount = jssCrawlerService.scrapeAndFilterJobs();
            return ResponseEntity.ok("Crawling completed. " + savedCount + " posts saved.");
        } catch (Exception e) {
            log.error("Error during crawling and saving: ", e);
            return ResponseEntity.internalServerError().body("An error occurred during the process.");
        }
    }

    @GetMapping("/generate-blog-post")
    public String generateBlogPost() {
        return recruitPostService.generateBlogPost();
    }
}