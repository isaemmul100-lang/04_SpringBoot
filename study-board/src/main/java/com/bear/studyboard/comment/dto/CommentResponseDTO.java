package com.bear.studyboard.comment.dto;

import com.bear.studyboard.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentResponseDTO {

    private Long postId;
    private String nickname;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponseDTO from(Comment comment) {
        return new CommentResponseDTO(
                comment.getStudyPost().getId(),
                comment.getAuthor().getNickname(),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
