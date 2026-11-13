package com.bear.springai.section01;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "01. Spring AI 시작하기") // 태그 이름의 앞 숫자에 따라 정렬이 됨
@RestController
@RequiredArgsConstructor
public class HelloAiController {

    private final ChatClient chatClient;

    @GetMapping("/section01/hello")
    public String hello() {

        /*
        * chatClient
        * - LLM 모델과 실제로 HTTP 통신을 하는 객체
        *
        * 동기호출
        * - call() LLM의 답변이 "전부" 완성될 때까지 기다렸다가 한번에 돌려줌
        * */

        return chatClient
                .prompt()            // 1) 프롬프트 요청을 작성
                .user("안녕.")   // 2) 사용자의 질문을 프롬프트에 추가
                .call()              // 3) LLM에게 전송하고 답변을 기다림 (동기)
                .content();          // 4) 답변에서 응답 텍스트만 꺼낸다.
    }
}
