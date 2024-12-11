package com.yh.jssblg.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yh.jssblg.service.JSSService;

@RestController
@RequestMapping("/api/jss")
@RequiredArgsConstructor
@Slf4j
public class JSSController {
    private final JSSService jssService;



    /**
     * 공고 데이터 수집
     * TODO: jssService.collectJobs()가 구현되면 해당 로직 실행
     */
    @PostMapping("/collect")
    public String collectJobs() {
        // TODO: 수집 서비스 호출 후 결과 메시지 반환
        jssService.collectJobs();
        return "Jobs collected successfully";
    }

    /**
     * 공고 목록 조회
     * TODO: page, size 파라미터를 활용하여 페이징 처리된 공고 데이터 반환
     */
    @GetMapping
    public Object getJobs(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size) {
        // TODO: jssService.getJobs(page, size)를 호출하고 반환
        return jssService.getJobs(page, size);
    }

    /**
     * 블로그 글 생성 및 발행
     * TODO: 요청 본문의 jobId를 기반으로 초안(draft) 여부에 따라 블로그 포스팅 로직 수행
     */
    @PostMapping("/blog/post")
    public String postToBlog(@RequestBody BlogPostRequestDTO requestBody) {
        // TODO: jssService.createAndPostBlog(jobId, draft) 실행 후 상태 메시지 반환
        boolean draft = requestBody.draft();
        String jobId = requestBody.jobId();
        jssService.createAndPostBlog(jobId, draft);
        return draft ? "Draft blog post created" : "Blog post published privately";
    }
    public record BlogPostRequestDTO(String jobId, boolean draft) {   
    }

}
