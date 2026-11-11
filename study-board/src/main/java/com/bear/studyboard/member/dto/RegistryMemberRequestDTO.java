package com.bear.studyboard.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistryMemberRequestDTO {

    private String email;
    private String nickname;

    @ToString.Exclude
    private String password;
}
