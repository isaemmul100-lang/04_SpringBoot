package com.bear.studyboard.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {

        // Advisor : AI 호출 앞뒤에 끼어들어 공통 작업을 하는 부품
        SimpleLoggerAdvisor loggerAdvisor = new SimpleLoggerAdvisor(); // LLM한테 보낸 응답, 출력

        ChatClient.Builder configurationBuilder = builder.defaultAdvisors(loggerAdvisor);

        return configurationBuilder.build();
    }
}
