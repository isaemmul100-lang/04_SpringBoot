package com.bear.studyboard.post.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostSummaryResponseDTO {

    private Long postId;
    private String title;
    private String summary; // AI가 작성한 요약
}
