package com.bear.studyboard.post.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.dto.PostSummaryResponseDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class PostSummaryService {

    private final StudyPostService studyPostService;
    private final ChatClient chatClient;

    /*
    * Resource : 파일이나 URL 같은 자원을 같은 방식으로 읽을 수 있게 해주는 인터페이스
    * */
    private final Resource systemPrompt;
    private final Resource userPrompt;

    // 1. DB 커넥션이 필요가 없음
    // 외부 API 호출은 트랜젝션 밖에서 한다.(AI, 결제, 메일 발송, ...)
    public PostSummaryService(
            StudyPostService studyPostService,
            ChatClient chatClient,
            @Value("classpath:/prompt/summary/system.st") Resource systemPrompt,
            @Value("classpath:/prompt/summary/user.st") Resource userPrompt) {
        this.studyPostService = studyPostService;
        this.chatClient = chatClient;
        this.systemPrompt = systemPrompt;
        this.userPrompt = userPrompt;
    }

    public PostSummaryResponseDTO summarize(long postId) {

        // 1. 게시글 찾아오기
        PostDetailResponseDTO postDTO = studyPostService.findPost(postId);

        // 2. AI한테 요청
        // 요청 만들기 (Prompt) -> 보내기 (처리) -> 답 꺼내기
        String content = "제목 : " + postDTO.getTitle() + System.lineSeparator()/*줄바꿈(엔터)*/ + postDTO.getBody();
        String summary;

        // 외부API 이므로 try-catch 써야함
        try {
            ChatClient.ChatClientRequestSpec request = chatClient.prompt()
                    .system(systemPrompt)
                    .user(user -> user.text(userPrompt)
                            .param("post", content));

            // LLM 모델에 요청을 보내고 응답을 받음
            ChatClient.CallResponseSpec response = request.call();

            summary = response.content();
        } catch (RuntimeException exception) {
            // 우리 예외로 바꾸기
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED, "잠시후 다시 시도하세요..");
        }

        // 3. AI 응답도 검증하기
        if (summary == null || summary.isBlank()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "요약결과가 비어있습니다.");
        }

        return new PostSummaryResponseDTO(postDTO.getId(), postDTO.getTitle(), summary);

    }
}
