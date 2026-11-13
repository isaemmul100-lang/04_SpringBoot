package com.bear.springai.section01;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "00. ")
@RestController
@RequestMapping("/section04/step01")
@RequiredArgsConstructor
public class DemoController {

    private final ChatClient chatClient;

    @GetMapping("/")
    public String hello(@RequestParam String question) {

        return chatClient
                .prompt()
                .user("안녕.")
                .call()
                .content();
    }
}
