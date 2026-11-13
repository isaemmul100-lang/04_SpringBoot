package com.bear.studyboard.post.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.post.dto.CreatePostProofreadingRequestDTO;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.service.PostAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostAIController {

    private final PostAIService postAIService;

    @PostMapping("/proofread")
    public ResponseEntity<ApiResponse<PostDetailResponseDTO>> createPostWithProofreading(
            @RequestBody CreatePostProofreadingRequestDTO request
            ) {
        PostDetailResponseDTO post = postAIService.createPostWithProofreading(request);

        HttpStatus status = HttpStatus.CREATED;
        ApiResponse<PostDetailResponseDTO> response = ApiResponse.of(status, "맞춤법 검사 후 저장 완료", post);

        return ResponseEntity.status(status).body(response);
    }
}
