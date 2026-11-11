package com.bear.studyboard.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostListItemResponseDto {

    private String authorName;
    private String title;
    private LocalDateTime createdAt;
}
