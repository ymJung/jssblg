package com.yh.jssblg.controller;

import com.yh.jssblg.domain.JobPostEntity;
import com.yh.jssblg.service.JSSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jss")
@RequiredArgsConstructor
@Slf4j
public class JSSController {

    private final JSSService jssService;

    /**
     * 공고 데이터 수집
     */
    @PostMapping("/collect")
    public ResponseEntity<String> collectJobs() {
        try {
            jssService.collectJobs();
            return ResponseEntity.ok("Jobs collected successfully");
        } catch (Exception e) {
            log.error("Failed to collect jobs: ", e);
            return ResponseEntity.internalServerError().body("Failed to collect jobs: " + e.getMessage());
        }
    }

    /**
     * 공고 목록 조회
     */
    @GetMapping
    public ResponseEntity<Page<JobPostEntity>> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<JobPostEntity> jobs = jssService.getJobs(page, size);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            log.error("Failed to get jobs: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 블로그 글 생성 및 발행
     */
    @PostMapping("/blog/post")
    public ResponseEntity<String> postToBlog(@RequestBody BlogPostRequestDTO requestBody) {
        try {
            boolean draft = requestBody.draft();
            String jobId = requestBody.jobId();
            jssService.createAndPostBlog(jobId, draft);
            String message = draft ? "Draft blog post created" : "Blog post published privately";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.error("Failed to create blog post: ", e);
            return ResponseEntity.internalServerError().body("Failed to create blog post: " + e.getMessage());
        }
    }

    public record BlogPostRequestDTO(String jobId, boolean draft) {
    }
}
