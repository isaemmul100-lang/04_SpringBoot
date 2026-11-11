package com.bear.studyboard.comment.controller;

import com.bear.studyboard.comment.dto.CommentListItemResponseDTO;
import com.bear.studyboard.comment.dto.CommentResponseDTO;
import com.bear.studyboard.comment.dto.CreateCommentRequestDTO;
import com.bear.studyboard.comment.service.CommentService;
import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.common.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(
            @RequestBody CreateCommentRequestDTO request,
            @PathVariable Long postId
    ) {

        CommentResponseDTO member = commentService.createComment(request, postId);

        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status)
                .body(ApiResponse.of(status, "댓글 등록 성공", member));
    }

    // 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<PageResponseDTO<CommentListItemResponseDTO>>> findPostComments(
            @PageableDefault(size = 20) Pageable pageable,
            @PathVariable Long postId
    ) {

        PageResponseDTO<CommentListItemResponseDTO> postPage = commentService.findPostComments(pageable, postId);

        HttpStatus status = HttpStatus.OK;

        return ResponseEntity.status(status).body(ApiResponse.of(HttpStatus.OK, "댓글 목록 조회 완료", postPage));
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> updateComment(
            @RequestBody String body,
            @PathVariable Long commentId
    ) {

        CommentResponseDTO member = commentService.updateComment(body, commentId);

        HttpStatus status = HttpStatus.OK;

        return ResponseEntity.status(status)
                .body(ApiResponse.of(status, "댓글 수정 성공", member));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }

}
