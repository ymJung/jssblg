package com.yh.jssblg.service;

import com.yh.jssblg.domain.JobPostEntity;
import com.yh.jssblg.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class JSSService {

    private final JobPostingRepository jobPostingRepository;
    private final BlogService blogService;
    private final WebClient webClient;

    /**
     * 공고 데이터 수집 로직
     *  1. jss.base.url 환경 변수로부터 기본 URL을 받아 자소설 사이트 특정 페이지 접근
     *  2. Jsoup 등을 사용해 HTML 파싱 후 공고 목록 추출
     *  3. 추출한 공고 데이터를 JobPosting 엔티티로 변환, jobPostingRepository.save()
     *  4. 중복 체크(logic) 필요 시 job_id로 조회 후 없을 경우에만 저장
     */
    @Transactional
    public void collectJobs() {
        log.info("Starting job collection process");
        try {
            webClient.get()
                    .uri("/api/jobs")  // TODO: Replace with actual endpoint
                    .retrieve()
                    .bodyToFlux(JobPostEntity.class)
                    .filter(job -> !jobPostingRepository.existsById(job.getId()))
                    .flatMap(job -> reactor.core.publisher.Mono.just(jobPostingRepository.save(job)))
                    .subscribe(
                            saved -> log.info("Saved job: {}", saved.getId()),
                            error -> log.error("Error saving job", error)
                    );
        } catch (Exception e) {
            log.error("Failed to collect jobs", e);
            throw new RuntimeException("Job collection failed", e);
        }
    }

    /**
     * 공고 목록 페이징 조회
     *  1. JpaRepository 페이징 메서드 사용 (findAll(Pageable))
     *  2. page, size 값 기반 Pageable 생성 후 결과 반환
     *  3. 반환 타입을 Page<엔티티> 또는 DTO로 변환해도 됨
     */
    @Transactional(readOnly = true)
    public Page<JobPostEntity> getJobs(int page, int size) {
        return jobPostingRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * 블로그 글 생성 및 발행 로직
     *  1. jobId로 공고 조회
     *  2. OpenAI API를 통한 내용 생성(blogService 또는 별도 helper 활용)
     *  3. 생성된 내용을 네이버 블로그 API를 통해 비공개 발행
     *  4. draft=true면 발행하지 않고 초안 상태 유지 로직(요구사항에 따라 조정)
     *  5. 발행한 공고는 중복 발행되지 않도록 DB에 상태 저장
     */
    @Transactional
    public void createAndPostBlog(String jobId, boolean draft) {
        JobPostEntity jobPost = jobPostingRepository.findById(Long.parseLong(jobId))
                .orElseThrow(() -> new RuntimeException("Job post not found: " + jobId));

        if (jobPost.isPostedToBlog()) {
            log.warn("Job {} already posted to blog", jobId);
            return;
        }

        String blogContent = generateBlogContent(jobPost);

        if (draft) {
            blogService.createDraft(jobPost.getJobTitle(), blogContent);
        } else {
            blogService.publishPost(jobPost.getJobTitle(), blogContent);
            jobPost.setPostedToBlog(true);
            jobPostingRepository.save(jobPost);
        }
    }

    private String generateBlogContent(JobPostEntity jobPost) {
        StringBuilder content = new StringBuilder();
        content.append("# ").append(jobPost.getJobTitle()).append("\n\n");
        content.append("## 채용 상세 정보\n\n");
        content.append("### 회사명\n");
        content.append(jobPost.getCompanyName()).append("\n\n");
        content.append("### 채용 내용\n");
        content.append(jobPost.getJobDescription()).append("\n\n");
        return content.toString();
    }
}
