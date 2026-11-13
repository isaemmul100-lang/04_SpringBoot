package com.bear.springai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    /*
    * ChatClient
    * - 프롬프트를 만들고, LLM 모델을 호출하고, 응답을 받는 API
    * - 해당 AI 모델의 프로바이더 서버에 HTTP 요청을 보내고 응답을 받아온다
    * */

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {

        /*
        * SimpleLoggerAdvisor : "LLM에게 실제로 무엇이 전송되는지" 확인하기 위해 모든 요청에 적용
        * */


        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
