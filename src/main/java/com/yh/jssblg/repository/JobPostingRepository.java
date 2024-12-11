package com.yh.jssblg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yh.jssblg.domain.JobPostEntity;

public interface JobPostingRepository extends JpaRepository<JobPostEntity, Long> {
    // TODO:
    //  1. job_id 중복 체크를 위한 findByJobId(...) 메서드 선언
    //  2. 특정 조건 필터링(조회수, 이력서 수 기준) 조회 메서드 필요 시 추가
    // ex) List<JobPosting> findByViewCountGreaterThanEqualAndResumeCountGreaterThanEqual(int view, int resume);
    JobPostEntity findByJobId(String jobId);
}