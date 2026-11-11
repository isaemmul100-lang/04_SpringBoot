package com.bear.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberUpdateRequestDTO {

    private String updateEmail;
    private String updateNickname;
}
