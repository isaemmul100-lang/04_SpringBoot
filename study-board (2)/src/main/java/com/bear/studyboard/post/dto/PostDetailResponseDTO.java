package com.bear.studyboard.post.dto;

import com.bear.studyboard.category.dto.CategoryResponseDTO;
import com.bear.studyboard.member.dto.AuthorResponseDTO;
import com.bear.studyboard.post.entity.StudyPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDetailResponseDTO {

    private long id;
    private String title;
    private String body;
    private AuthorResponseDTO author;
    private CategoryResponseDTO category;
    private String referenceUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostDetailResponseDTO(StudyPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.author = AuthorResponseDTO.from(post.getAuthor());
        this.category = CategoryResponseDTO.from(post.getCategory());
        this.referenceUrl = post.getReferenceUrl();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public static PostDetailResponseDTO from(StudyPost post) {
        return new PostDetailResponseDTO(post);
    }
}
