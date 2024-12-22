package com.yh.jssblg.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.yh.jssblg.config.ApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogService {
    private final ApiConfig apiConfig;
    private final WebClient webClient;
    private final OpenAiService openAiService;

    public String generateBlogContent(String companyName, int viewCount, int resumeCount) {
        String prompt = String.format(
            "Create a blog post about job posting from %s company. " +
            "This post has %d views and %d resume submissions. " +
            "Include analysis about job market trends.",
            companyName, viewCount, resumeCount
        );

        CompletionRequest request = CompletionRequest.builder()
            .model("gpt-3.5-turbo-instruct")
            .prompt(prompt)
            .maxTokens(500)
            .build();

        return openAiService.createCompletion(request)
            .getChoices().get(0).getText();
    }

    public void postToNaverBlog(String title, String content, boolean draft) {
        String apiUrl = "https://openapi.naver.com/blog/writePost.json";
        
        NaverBlogRequest request = new NaverBlogRequest(title, content, draft ? "0" : "1");

        webClient.post()
            .uri(apiUrl)
            .header("Authorization", "Bearer " + apiConfig.getNaverAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public void createDraft(String jobTitle, String blogContent) {
        postToNaverBlog(jobTitle, blogContent, true);
    }

    public void publishPost(String jobTitle, String blogContent) {
        postToNaverBlog(jobTitle, blogContent, false);
    }

    private record NaverBlogRequest(String title, String content, String visibility) {}
}
