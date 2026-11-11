package com.bear.studyboard.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentListItemResponseDTO {

    private String authorName;
    private String body;
    private LocalDateTime createdAt;
}
