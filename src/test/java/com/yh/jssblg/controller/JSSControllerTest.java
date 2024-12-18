package com.yh.jssblg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yh.jssblg.domain.JobPostEntity;
import com.yh.jssblg.service.JSSService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JSSController.class)
class JSSControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JSSService jssService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("채용공고 목록 조회 - 성공")
    void getJobs_Success() throws Exception {
        // Given
        List<JobPostEntity> jobList = List.of(
            createJobPost(1L, "Backend Developer", "Company A"),
            createJobPost(2L, "Frontend Developer", "Company B")
        );
        Page<JobPostEntity> jobPage = new PageImpl<>(jobList);
        given(jssService.getJobs(anyInt(), anyInt())).willReturn(jobPage);

        // When & Then
        mockMvc.perform(get("/api/jss")
                .param("page", "0")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Backend Developer"));
    }

    @Test
    @DisplayName("채용공고 수집 - 성공")
    void collectJobs_Success() throws Exception {
        // Given
        doNothing().when(jssService).collectJobs();

        // When & Then
        mockMvc.perform(post("/api/jss/collect"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Jobs collected successfully"));
    }

    @Test
    @DisplayName("블로그 포스팅 - 성공")
    void postToBlog_Success() throws Exception {
        // Given
        JSSController.BlogPostRequestDTO requestDTO = new JSSController.BlogPostRequestDTO("1", true);
        doNothing().when(jssService).createAndPostBlog("1", true);

        // When & Then
        mockMvc.perform(post("/api/jss/blog/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Draft blog post created"));
    }

    @Test
    @DisplayName("블로그 포스팅 - 실패 (잘못된 요청)")
    void postToBlog_Failure_BadRequest() throws Exception {
        // Given
        JSSController.BlogPostRequestDTO requestDTO = new JSSController.BlogPostRequestDTO(null, true);

        // When & Then
        mockMvc.perform(post("/api/jss/blog/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private JobPostEntity createJobPost(Long id, String title, String companyName) {
        JobPostEntity job = new JobPostEntity();
        job.setId(id);
        job.setCompanyName(companyName);
        return job;
    }
} 