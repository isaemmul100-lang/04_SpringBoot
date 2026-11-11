package com.bear.studyboard.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 이메일 중복가입이 안된다.

    @Column(name = "password_hash", nullable = false)
    @Getter(AccessLevel.NONE) // getter 접근 불가
    private String passwordHash;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime drawnAt;

    // 첫 회원가입 시 사용할 생성자
    public Member(String email, String passwordHash, String nickname, LocalDateTime joinAt) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.joinAt = joinAt;
    }

    // 패스워드만 조회 가능한 메서드를 명시
    public String PasswordHash() {
        return passwordHash;
    }

    // Entity의 수정 같은 경우
    // Setter를 사용하지 않고 수정하는 내용을 명시할 수 있는 메서드를 따로 만들어준다.
    // 회원 수정
    public void updateProfile(String email, String nickname) {
        if(email != null) {
            this.email = email;
        }
        if (nickname != null) {
            this.nickname = nickname;
        }
    }

    // 회원 탈퇴
    public void deleteProfile(LocalDateTime drawnAt) {
        if(email != null) {
            this.drawnAt = drawnAt;
        }
    }
}
