package com.bear.studyboard.quiz.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.service.StudyPostService;
import com.bear.studyboard.quiz.dto.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizAIService {

    private static final int DEFAULT_QUIZ_COUNT = 3; // 문제 생성갯수의 기본값은 3
    private static final int MAX_QUIZ_COUNT = 5; // 문제 생성갯수의 최대값은 5

    private final StudyPostService studyPostService;
    private final QuizService quizService;
    private final ChatClient chatClient;
    private final Resource generateQuizSystemPrompt;
    private final Resource generateQuizUserPrompt;
    private final QuizAIClient quizAIClient;

    public QuizAIService(
            StudyPostService studyPostService,
            ChatClient chatClient, QuizService quizService,
            @Value("classpath:/prompt/generateQuiz/system.st") Resource generateQuizSystemPrompt,
            @Value("classpath:/prompt/generateQuiz/user.st") Resource generateQuizUserPrompt, QuizAIClient quizAIClient) {
        this.studyPostService = studyPostService;
        this.chatClient = chatClient;
        this.quizService = quizService;
        this.generateQuizSystemPrompt = generateQuizSystemPrompt;
        this.generateQuizUserPrompt = generateQuizUserPrompt;
        this.quizAIClient = quizAIClient;
    }

    public List<QuizResponseDTO> generateQuizzes(long postId, Integer requestCount) {

        // 검증
        int count = resolveCount(requestCount);

        // 문제 만들기
        // 1. 프롬프트 재료준비
        PostDetailResponseDTO post = studyPostService.findPost(postId);
        String title = post.getTitle();
        String body = post.getBody();

        // 프롬프트 요청
        GeneratedQuizSetDTO generated = generatedQuizzes(title, body, count);

        // AI가 응답한 결과를 검증
        List<GeneratedQuizDTO> validQuizzes = validateGeneratedQuizzes(generated, count);

        // 저장
        // List<QuizResponseDTO> 응답
        return quizService.saveQuizzes(post.getId(), validQuizzes);
    }

    public GeneratedQuizSetDTO generatedQuizzes(String title, String body, int count) {

        // 프롬프트 요청 준비하기
        try {
            ChatClient.ChatClientRequestSpec request = chatClient.prompt()
                    .system(generateQuizSystemPrompt)
                    .user(u -> u.text(generateQuizUserPrompt)
                            .param("body", body)
                            .param("title", title)
                            .param("count", String.valueOf(count)));

            // 응답 받기
            ChatClient.CallResponseSpec response = request.call();

            // 원하는 형태로 응답 가공하기
            return response.entity(GeneratedQuizSetDTO.class);

        } catch (RuntimeException exception) {
            throw new BusinessException(ErrorCode.AI_REQUEST_FAILED);
        }
    }

    // 검증로직

    private static List<GeneratedQuizDTO> validateGeneratedQuizzes(GeneratedQuizSetDTO generated, int count) {

        // 목록이 없는 경우
        if (generated == null || generated.getQuizzes() == null) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "생성된 문제가 없습니다.");
        }

        // 목록을 검증한 후 있는 검증된 내용들만 담아줄 리스트 준비
        List<GeneratedQuizDTO> generatedQuizzes = generated.getQuizzes();
        List<GeneratedQuizDTO> validQuizzes = new ArrayList<>();

        for (GeneratedQuizDTO quiz : generatedQuizzes) {
            if (quiz == null) {
                continue;
            }
            String question = quiz.getQuestion();
            String answer = quiz.getAnswer();

            if (question == null || question.isBlank() || question.length() > 500) {
                continue;
            }

            if (answer == null || answer.isBlank() || answer.length() > 500) {
                continue;
            }

            validQuizzes.add(quiz);

            if (validQuizzes.size() == count) {
                break;
            }
        }

        if (validQuizzes.isEmpty()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "저장할 수 있는 문제가 없습니다.");
        }

        return validQuizzes;
    }

    private static int resolveCount(Integer count) {
        if (count == null) {
            count = DEFAULT_QUIZ_COUNT;
        }

        if (count < 1 || count > MAX_QUIZ_COUNT) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "문제수는 1개 이상 " + MAX_QUIZ_COUNT + "개 이하여야합니다.");

        }

        return count;
    }


    public QuizGradeResponseDTO gradeQuiz(long quizId, String answer) {
        // 1. 요청값 확인
        // 답변이 비어있을 때 -> 2이하, 1이하

        if (answer == null || answer.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "답을 입력해주세요.");
        }

        if (answer.length() > 500 || answer.length() < 2) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "답을 입력해주세요.");
        }

        // 2. 문제 정보를 가져오기
        QuizResponseDTO quizResponseDTO = quizService.findQuiz(quizId);

        // 3. 채점
        // 3-1. AI를 부르지 않아도 되는 경우
        String correctAnswer = quizResponseDTO.getAnswer();
        if (answer.equals(correctAnswer)) {
            return new QuizGradeResponseDTO(quizId, true, "정답과 일치 합니다.", answer, "EXACT_MATCH");
        }

        // 3-2. AI를 불러야하는 경우
        String question = quizResponseDTO.getQuestion();
        AiGradeResultDTO result = quizAIClient.grade(question, correctAnswer, answer);

        String gradingModel = quizAIClient.gradingModel();

        // 검증
        if (result == null || result.getFeedback() == null || result.getFeedback().isBlank()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "채점 결과가 비어있습니다.");
        }

        return new QuizGradeResponseDTO(quizId, result.isCorrect(), result.getFeedback(), correctAnswer, gradingModel);
    }
}
