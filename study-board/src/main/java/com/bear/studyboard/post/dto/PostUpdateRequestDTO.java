package com.bear.studyboard.post.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostUpdateRequestDTO {

    private String title;
    private String body;
    private Long category;
    private String url;

}
