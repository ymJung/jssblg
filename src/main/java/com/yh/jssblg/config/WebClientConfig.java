package com.yh.jssblg.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * TODO:
 *  1. WebClient Bean 생성
 *  2. 공통 헤더 설정(필요한 경우)
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // TODO: WebClient.Builder 활용해 기본 설정
        return WebClient.builder().build();
    }
}