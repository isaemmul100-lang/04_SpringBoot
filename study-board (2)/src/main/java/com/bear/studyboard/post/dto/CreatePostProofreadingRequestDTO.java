package com.bear.studyboard.post.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatePostProofreadingRequestDTO {

    private String title;
    private String body;
    private Long categoryId;
    private Long authorId;
    private String referenceUrl;
}
