package com.yh.jssblg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * TODO:
 *  1. application-openapi.yml에서 OpenAI API Key 및 URL을 @Value로 주입
 *  2. RestTemplate 또는 WebClient를 활용해 OpenAI API 호출용 Bean 정의
 */
@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    // TODO: OpenAI API 호출용 WebClient Bean 생성 등
}