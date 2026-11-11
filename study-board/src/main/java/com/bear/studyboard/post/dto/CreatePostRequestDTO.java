package com.bear.studyboard.post.dto;

import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.member.entity.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreatePostRequestDTO {

    private String title;
    private String body;
    private Long category;
    private Long author;
    private String referenceUrl;


}
