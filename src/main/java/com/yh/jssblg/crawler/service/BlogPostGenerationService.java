package com.yh.jssblg.crawler.service;

import com.yh.jssblg.crawler.model.RecruitPostDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import com.google.gson.Gson;

@Service
@Slf4j
public class BlogPostGenerationService {
    @Value("${openai.api.key}") 
    private String openaiApiKey;
    private Gson gson = new Gson();

    public String generateBlogPost(RecruitPostDTO recruitPost) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("${openai.api.url}"))
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${openai.api.key}")
                .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(recruitPost)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // JSON    
                // ... (JSON 파싱 로직 추가)
                log.info("Blog post generated successfully: " + response.body());
                return response.body();
            } else {
                log.error("Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error: " + e.getMessage(), e);
        }
        return "";
    }

    private String buildRequestBody(RecruitPostDTO recruitPost) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a helpful AI assistant."),
                Map.of("role", "user", "content", "Generate a blog post draft about a job opening at " + recruitPost.getCompanyName() + 
                        " for the position of " + recruitPost.getTitle() + ".")
        ));


        return gson.toJson(requestBody); // Gson 또는 다른 JSON 라이브러리 사용
    }
       
}

