package com.bear.studyboard.post.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.common.dto.PageResponseDTO;
import com.bear.studyboard.post.dto.CreatePostRequestDTO;
import com.bear.studyboard.post.dto.PostDetailResponseDto;
import com.bear.studyboard.post.dto.PostListItemResponseDto;
import com.bear.studyboard.post.dto.PostUpdateRequestDTO;
import com.bear.studyboard.post.service.StudyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class StudyPostController {

    private final StudyPostService studyPostService;

    /*
     * 페이징 (Paging)
     * - 데이터를 전부 주지 않고 몇 번째 페이지를 한 번에 몇 페이지씩 나누어 주는 방식
     *
     * Pageable -> 페이지 번호, 페이지 크기, 정렬 정보
     * - Spring Data가 제공하는 "페이지 요청 정보" 인터페이스
     * - /api/posts?page=1&size=10&sort=createdAt, desc
     * page -> 페이지 번호(0부터)
     * size -> 한번에 담을 갯수
     * sort -> 정렬기준
     * */


    // 자료 등록
    @PostMapping
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> createPost(
            @RequestBody CreatePostRequestDTO request
    ) {

        PostDetailResponseDto newPost = studyPostService.createPost(request);

        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status).body(ApiResponse.of(status, "게시물 등록 완료", newPost));
    }

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponseDTO<PostListItemResponseDto>>> findPosts(
            @PageableDefault(size = 20) Pageable pageable
    ) {

        PageResponseDTO<PostListItemResponseDto> postPage = studyPostService.findPosts(pageable);

        return ResponseEntity.ok(ApiResponse.of(HttpStatus.OK, "게시물 목록 조회 완료", postPage));
    }

    // 자료 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> findPostById(
            @PathVariable Long postId
    ) {

        PostDetailResponseDto postPage = studyPostService.findPostById(postId);

        HttpStatus status = HttpStatus.OK;

        return ResponseEntity.status(status).body(ApiResponse.of(status, "자료 상세 조회 완료", postPage));
    }

    // 자료 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> updatePost(
            @RequestBody PostUpdateRequestDTO request,
            @PathVariable Long postId
    ) {

        PostDetailResponseDto postPage = studyPostService.updatePost(request, postId);

        HttpStatus status = HttpStatus.OK;

        return ResponseEntity.status(status).body(ApiResponse.of(status, "자료 수정 완료", postPage));
    }
}
