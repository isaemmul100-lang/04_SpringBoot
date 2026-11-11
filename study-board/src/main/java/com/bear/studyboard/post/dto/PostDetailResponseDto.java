package com.bear.studyboard.post.dto;

import com.bear.studyboard.post.entity.StudyPost;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDetailResponseDto {

    private String title;
    private String body;
    private String referenceUrl;
    private String categoryName;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDetailResponseDto from(StudyPost studyPost) {
        return new PostDetailResponseDto(
                studyPost.getTitle(),
                studyPost.getBody(),
                studyPost.getReferenceUrl(),
                studyPost.getCategory().getName(),
                studyPost.getAuthor().getNickname(),
                studyPost.getCreatedAt(),
                studyPost.getUpdatedAt()
        );
    }
}
