package com.yh.jssblg.crawler.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.jssblg.crawler.model.RecruitJobDTO;

import lombok.extern.slf4j.Slf4j;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class JSSCrawlerService {
    @Value("${jss.base.url}")
    private String BASE_URL;


   public List<RecruitJobDTO> scrapeAndFilterJobs() {
        List<RecruitJobDTO> filteredJobs = new ArrayList<>();
        try {
            // 메인 페이지에서 채용 공고 목록 가져오기
            Document mainPageDoc = Jsoup.connect(BASE_URL).get();

            // 채용 공고 요소 선택 (실제 클래스나 셀렉터는 사이트 구조에 맞게 수정 필요)
            Elements jobElements = mainPageDoc.select(".recruit-item a");
            log.info("target job elements count : {}", jobElements.size());
            for (Element jobElement : jobElements) {
                String href = jobElement.attr("href");
                // 채용 ID 추출
                String jobId = href.replace("/recruit/", "");

                // JSON 데이터 가져오기
                String jsonUrl = BASE_URL + jobId + "/get.json";
                String jsonData = Jsoup.connect(jsonUrl)
                        .ignoreContentType(true)
                        .execute()
                        .body();

                // JSON 파싱
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(jsonData);

                int viewCount = rootNode.path("view_count").asInt();
                int resumeCount = rootNode.path("resume_count").asInt();
                log.info("jobId : {}, viewCount : {}, resumeCount : {}", jobId, viewCount, resumeCount);
                // 필터링 로직
                if (viewCount > 1000 && resumeCount > 100) {
                    RecruitJobDTO recruitJobDTO = new RecruitJobDTO();
                    recruitJobDTO.setId(jobId);
                    recruitJobDTO.setTitle(rootNode.path("title").asText());
                    recruitJobDTO.setViewCount(viewCount);
                    recruitJobDTO.setResumeCount(resumeCount);
                    // 추가 필드 설정 가능

                    filteredJobs.add(recruitJobDTO);
                    log.info("Filtered job: " + recruitJobDTO);
                }
            }
        } catch (Exception e) {            
            log.error("Error: " + e.getMessage(), e);
        }
        return filteredJobs;
    }


}
