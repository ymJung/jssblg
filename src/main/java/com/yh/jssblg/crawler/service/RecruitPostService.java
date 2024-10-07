package com.yh.jssblg.crawler.service;

import org.springframework.stereotype.Service;

import com.yh.jssblg.crawler.model.RecruitJobDTO;

@Service
public class RecruitPostService {

    public String generateBlogPost(RecruitJobDTO recruitJobDTO) {
         // OpenAI API를 사용하여 블로그 포스트 생성
        String prompt = "다음 채용 공고에 대한 블로그 포스트를 작성해주세요:\n";
        prompt += "제목: " + recruitJobDTO.getTitle() + "\n";
        prompt += "조회수: " + recruitJobDTO.getViewCount() + "\n";
        prompt += "지원자 수: " + recruitJobDTO.getResumeCount() + "\n";
        // 추가 정보 포함 가능

        // OpenAI API 호출 로직 (실제 API 호출 코드 필요)
        // 예시로 간단한 문자열 반환
        String blogPost = "이곳에 OpenAI API를 통해 생성된 블로그 포스트 내용이 들어갑니다.";

        return blogPost;
    }
}
