package com.bear.studyboard.post.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.post.dto.CreatePostProofreadingRequestDTO;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.dto.PostProofreadingResultDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class PostAIClient {

    private final ChatClient chatClient;
    private final Resource userPrompt;
    private final Resource systemPrompt;

    public PostAIClient(
            ChatClient chatClient,
            @Value("classpath:/prompt/proofreading/system.st") Resource systemPrompt,
            @Value("classpath:/prompt/proofreading/user.st") Resource userPrompt) {
        this.chatClient = chatClient;
        this.userPrompt = userPrompt;
        this.systemPrompt = systemPrompt;
    }

    public PostProofreadingResultDTO proofreading(String title, String body) {

        // 프롬프트 요청 준비
        try {
            ChatClient.ChatClientRequestSpec request = chatClient.prompt()
                    .system(systemPrompt)
                    .user(u->u
                            .text(userPrompt)
                            .param("title", title)
                            .param("body", body));

            // 응답 받기
            ChatClient.CallResponseSpec response = request.call();

            // 원하는 형태로 응답 가공하기
            return response.entity(PostProofreadingResultDTO.class);

        } catch (RuntimeException exception) {
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }
    }
}
