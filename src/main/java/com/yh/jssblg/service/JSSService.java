package com.yh.jssblg.service;

import org.springframework.stereotype.Service;

import com.yh.jssblg.repository.JobPostingRepository;

@Service
public class JSSService {

    private final JobPostingRepository jobPostingRepository;
    private final BlogService blogService;
    // TODO: 필요한 경우 HTTP 클라이언트(WebClient), Jsoup, OpenAI/네이버 API 서비스 Bean 주입

    public JSSService(JobPostingRepository jobPostingRepository, BlogService blogService) {
        this.jobPostingRepository = jobPostingRepository;
        this.blogService = blogService;
    }

    /**
     * 공고 데이터 수집 로직
     * TODO:
     *  1. jss.base.url 환경 변수로부터 기본 URL을 받아 자소설 사이트 특정 페이지 접근
     *  2. Jsoup 등을 사용해 HTML 파싱 후 공고 목록 추출
     *  3. 추출한 공고 데이터를 JobPosting 엔티티로 변환, jobPostingRepository.save()
     *  4. 중복 체크(logic) 필요 시 job_id로 조회 후 없을 경우에만 저장
     */
    public void collectJobs() {
        // TODO: 자소설 사이트 파싱 및 공고 데이터 저장 로직 구현
    }

    /**
     * 공고 목록 페이징 조회
     * TODO:
     *  1. JpaRepository 페이징 메서드 사용 (findAll(Pageable))
     *  2. page, size 값 기반 Pageable 생성 후 결과 반환
     *  3. 반환 타입을 Page<엔티티> 또는 DTO로 변환해도 됨
     */
    public Object getJobs(int page, int size) {
        // TODO: Pageable 생성 및 jobPostingRepository.findAll(pageable) 호출
        return jobPostingRepository.findAll(); 
    }

    /**
     * 블로그 글 생성 및 발행 로직
     * TODO:
     *  1. jobId로 공고 조회
     *  2. OpenAI API를 통한 내용 생성(blogService 또는 별도 helper 활용)
     *  3. 생성된 내용을 네이버 블로그 API를 통해 비공개 발행
     *  4. draft=true면 발행하지 않고 초안 상태 유지 로직(요구사항에 따라 조정)
     *  5. 발행한 공고는 중복 발행되지 않도록 DB에 상태 저장
     */
    public void createAndPostBlog(String jobId, boolean draft) {
        // TODO: 공고 조회 -> AI 컨텐츠 생성 -> 블로그 발행
    }
}
