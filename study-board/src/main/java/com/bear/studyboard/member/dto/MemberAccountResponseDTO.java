package com.bear.studyboard.member.dto;

import com.bear.studyboard.member.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 회원가입 후 응답에 사용할 DTO
public class MemberAccountResponseDTO {

    private long id;
    private String email;
    private String nickname;
    private LocalDateTime joinedAt;
    private LocalDateTime withdrawnAt;

    // Entity -> DTO
    public static MemberAccountResponseDTO from(Member member) {
        return new MemberAccountResponseDTO(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getJoinAt(),
                member.getDrawnAt() == null ? null : member.getDrawnAt());
    }
}
