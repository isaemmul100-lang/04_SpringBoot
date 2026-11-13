package com.bear.springai.section02;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "02-02. Prompt")
@RestController
public class Step03SystemDefaultController {

    private final ChatClient chatClient;

    public Step03SystemDefaultController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("너는 스터디 게시판의 도우미이다. 항상 한국어로 2문장 이내로 답한다.")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    // system을 지정 안 했을 때는 defaultSystem의 내용이  System 메시지로 전달됨.
    @GetMapping("/section02/step03")
    public String step03(@RequestParam String question) {

        return chatClient
                .prompt()
                .user(question)
                .call()
                .content();
    }

    // defaultSystem 은 무시되고 System 메시지가 변경되어 전달됨
    @GetMapping("/section02/step03/overide")
    public String step03_1(@RequestParam String question) {

        return chatClient
                .prompt()
                .system("Answer in English, in one short sentence")
                .user(question)
                .call()
                .content();
    }
}
