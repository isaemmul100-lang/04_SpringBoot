package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "테스트 메소드 입니다.";
    }

    @GetMapping(value = "/index", produces = "text/html; charset = UTF-8")
    public String test2() {
        return """
                <h1>인덱스 화면입니다.</h1>
                <h3>이건 예시입니다</h3>
                """;
    }
}
