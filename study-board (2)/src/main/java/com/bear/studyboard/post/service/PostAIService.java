package com.bear.studyboard.post.service;

import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.category.service.CategoryService;
import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.member.service.MemberService;
import com.bear.studyboard.post.dto.CreatePostProofreadingRequestDTO;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.dto.PostProofreadingResultDTO;
import com.bear.studyboard.post.entity.StudyPost;
import com.bear.studyboard.post.repository.StudyPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostAIService {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final StudyPostRepository studyPostRepository;
    private final PostAIClient postAIClient;

    public PostDetailResponseDTO createPostWithProofreading(CreatePostProofreadingRequestDTO request) {

        Member author = memberService.requireActiveMember(request.getAuthorId());

        Category category = categoryService.findRequiredCategory(request.getCategoryId());

        // 프롬프트 요청 준비하기
        String title = request.getTitle();
        String body = request.getBody();

        // 프롬프트 요청
        PostProofreadingResultDTO proofreading = postAIClient.proofreading(title, body);

        // AI가 응답한 결과를 검증
        if (proofreading == null || proofreading.getTitle() == null || proofreading.getBody() == null) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_INVALID, "맞춤법 검사 결과가 없습니다.");
        }

        StudyPost post = new StudyPost(
                author,
                category,
                proofreading.getTitle(),
                proofreading.getBody(),
                request.getReferenceUrl(),
                LocalDateTime.now());

        StudyPost savedPost = studyPostRepository.save(post);

        return PostDetailResponseDTO.from(savedPost);
    }

}
