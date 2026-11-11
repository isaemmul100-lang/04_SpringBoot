package com.bear.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDeleteRequestDTO {

    @ToString.Exclude
    private String password;
}
