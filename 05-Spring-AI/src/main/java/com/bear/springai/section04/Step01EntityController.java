package com.bear.springai.section04;

import com.bear.springai.section04.dto.PostAnalysis;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * LLM의 답변은 문자열이기 때문에 객체로 전달하기 위해서는 변환해주어야한다.
 *
 * .entity(DTO 클래스)
 * - 호출되면 답변이 DTO 객체로 돌아온다.
 *
 * 내부에서의 동작
 * 1. String AI가 PostAnalysis.class의 구조를 읽어 "이 JSON 형식으로만 답하라"라는 지시문을 만듦.
 * 2. 지시문을 프롬프트 끝에 자동으로 덧붙여 전송
 * 3. LLM이 JSON 문자열로 응답
 * 4. Spring AI가 DTO 객체를 만들어 JSON값을 채움
 * */
@Tag(name = "04-01. 객체로 응답받기")
@RestController
@RequestMapping("/section04/step01")
@RequiredArgsConstructor
public class Step01EntityController {

    private final ChatClient chatClient;

    private static final String POST = """
            JPA로 게시글 목록을 조회하는데 쿼리가 수십 번 나가서 원인을 찾아봤습니다.
            게시글마다 작성자를 지연 로딩으로 가져오면서 N+1 문제가 발생한 것이었고,
            fetch join으로 바꾸니 쿼리 한 번으로 줄었습니다. 같은 문제 겪는 분들께 도움이 되길 바랍니다.
            """;

    @GetMapping
    public PostAnalysis hello() {

        PostAnalysis postAnalysis = chatClient
                .prompt()
                .system("""
                        <post> 태그 안의 게시글을 분석한다.
                        - summary: 한 문장 요약
                        - category: [공지, 질문, 후기, 잡담] 중 하나
                        - tags: 어울리는 태그 3개
                        """)
                .user(u -> u
                        .text("<post>{post}</post>")
                        .param("post", POST))
                .call()
                // 리플렉션 : 프로그램 실행 중에 클래스의 구조를 조회하거나 조작하는 JAVA 기능 - 보통 프레임워크 만들 경우에 사용
                .entity(PostAnalysis.class); // 답변을 PostAnalysis 객체로 변환해서 돌려줌

        return postAnalysis;
    }
}
