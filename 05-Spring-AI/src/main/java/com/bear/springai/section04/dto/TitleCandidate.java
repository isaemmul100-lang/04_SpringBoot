package com.bear.springai.section04.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// Spring AI 객체를 생성하고 값을 채울 수 있도록 기본생성자 + setter 필수!
public class TitleCandidate {

    // 추천 제목과 추천 이유 전달하는 DTO
    private String title;
    private String reason;
}
