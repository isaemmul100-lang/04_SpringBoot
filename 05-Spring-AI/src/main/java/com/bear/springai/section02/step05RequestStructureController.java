package com.bear.springai.section02;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "02-03. 요청구조")
@RestController
public class step05RequestStructureController {

    private final ChatModel chatModel;
    private final ResourcePatternResolver resourcePatternResolver;

    public step05RequestStructureController(ChatModel chatModel, ResourcePatternResolver resourcePatternResolver) {
        this.chatModel = chatModel;
        this.resourcePatternResolver = resourcePatternResolver;
    }

    @GetMapping("/section02/step05")
    public String hello(@RequestParam String question) {

        // 1. 역할별 메시지 객체를 만든다. (.system() / .user())
        SystemMessage systemMessage = new SystemMessage("너는 신입 개발자를 가르치는 강사다. 2문장 이내로 답해라");
        UserMessage userMessage = new UserMessage(question);

        // 2. 메시지 목록을 하나의 Prompt로 묶음
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // 3. ChatModel이 Prompt를 HTTP 요청으로 바꿔 OpenAI에게 전송
        ChatResponse response = chatModel.call(prompt);

        System.out.println("response.getResult() = " + response.getResult());
        /*
        * 출력결과
        * response.getResult() = Generation[
        *   assistantMessage=AssistantMessage
        *       [
        *           messageType=ASSISTANT,
        *           toolCalls=[],
        *           textContent=트랜잭션은 데이터베이스에서 일련의 작업을 하나의 단위로 묶어 처리하는 것을 의미합니다. 이를 통해 데이터의 일관성과 무결성을 유지할 수 있습니다.,
        *           metadata=
        *               {
        *                   role=assistant,
        *                   messageType=ASSISTANT,
        *                   finishReason=STOP,
        *                   refusal=,
        *                   annotations=[],
        *                   index=0,
        *                   id=chatcmpl-EjHAYKc0FoHK28SXclXzd066AojqF,
        *                   reasoningContent=
        *               }
        *           ],
        *   chatGenerationMetadata=DefaultChatGenerationMetadata
        *       [
        *           finishReason='STOP',
        *           filters=0,
        *           metadata=0
        *       ]
        * ]
        * */

        System.out.println("response.getResult().getOutput() = " + response.getResult().getOutput());
        /*
        * 출력결과
        * response.getResult().getOutput() = AssistantMessage [messageType=ASSISTANT, toolCalls=[],
        * textContent=트랜잭션은 데이터베이스에서 일련의 작업을 하나의 단위로 묶어 처리하는 것을 의미합니다. 이를 통해 데이터의 일관성과 무결성을 유지할 수 있습니다.,
        * metadata={role=assistant, messageType=ASSISTANT, finishReason=STOP, refusal=, annotations=[], index=0,
        * id=chatcmpl-EjHAYKc0FoHK28SXclXzd066AojqF, reasoningContent=}]
        * */

        // 4. ChatResponse에서 답변 텍스트를 꺼내기
        String answer = response.getResult().getOutput().getText();
        System.out.println("answer = " + answer);
        /*
        * 출력결과
        * answer = 트랜잭션은 데이터베이스에서 일련의 작업을 하나의 단위로 묶어 처리하는 것을 의미합니다. 이를 통해 데이터의 일관성과 무결성을 유지할 수 있습니다.
        * */

        /*
         * 토큰 사용량 확인
         * - PromptTokens: 입력 토큰 수
         * - CompletionTokens: 출력 토큰 수
         * - TotalTokens: 전체 사용 토큰 수 (입력 + 출력)
         */
        System.out.println("answer = " + response.getMetadata().getUsage().getPromptTokens()); // 입력토큰
        System.out.println("answer = " + response.getMetadata().getUsage().getCompletionTokens()); // 출력토큰
        System.out.println("answer = " + response.getMetadata().getUsage().getTotalTokens()); // 전체토큰

        return "";
    }
}
