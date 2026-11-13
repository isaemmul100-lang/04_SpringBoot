package com.bear.studyboard.post.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.post.dto.PostDetailResponseDTO;
import com.bear.studyboard.post.dto.PostSummaryResponseDTO;
import com.bear.studyboard.post.service.PostSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostSummaryController {

    private final PostSummaryService postSummaryService;

    /*
     * GET이 아니라 POST를 쓰는 이유
     * - GET은 몇 번 호출해도 겨로가가 같고, 서버에 부담을 주지 않는 조회에서 사용
     * - 요약은 호출할 때마다 비용이 발생하고 결과가 달라질 수 있음 -> POST 사용근거
     * */
    @PostMapping("/{postId}/summary")
    public ResponseEntity<ApiResponse<PostSummaryResponseDTO>> summary(@PathVariable long postId) {

        PostSummaryResponseDTO summary = postSummaryService.summarize(postId);

        ApiResponse<PostSummaryResponseDTO> response = ApiResponse.of(HttpStatus.OK, "게시글 요약완료", summary);

        return ResponseEntity.ok(response);
    }

}
