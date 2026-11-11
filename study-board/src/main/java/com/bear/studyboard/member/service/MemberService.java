package com.bear.studyboard.member.service;

import com.bear.studyboard.common.error.BusinessException;
import com.bear.studyboard.common.error.ErrorCode;
import com.bear.studyboard.member.dto.*;
import com.bear.studyboard.member.entity.Member;
import com.bear.studyboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberAccountResponseDTO register(RegistryMemberRequestDTO request) {
        // 회원가입 - email은 unique 중복일경우 예외 던지기
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        // 패스워드를 암호화
        // Bcrypt : 무작위 솔트를 만들고, 솔트와 입력 비밀번호로 해시를 계산한다.
        // 같은 비밀번호로 가입해도 솔트가 다르면 해시가 달라짐 -> 반환문자열에 솔트와 해시가 함께 들어감 (이걸 DB에 저장)
        String passwordHash = encodeNewPassword(request.getPassword());
        System.out.println("passwordHash = " + passwordHash);

        // Entity 객체 생성
        Member member = new Member(
                request.getEmail(),
                passwordHash,
                request.getNickname(),
                LocalDateTime.now());

        Member savedMember = memberRepository.saveAndFlush(member); // 저장된 객체(영속) 반환

        return MemberAccountResponseDTO.from(savedMember);
    }

    public String encodeNewPassword(String password) {
        try {
            return passwordEncoder.encode(password);
        } catch (IllegalArgumentException exception) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "비밀번호는 UTF-8 기준 72바이트 이하여야 합니다.");
        }
    }

    // 로그인 메서드
    public MemberAccountResponseDTO login(LoginMemberRequestDTO request) {

        String email = request.getEmail();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        if (member.getDrawnAt() != null) {
            throw new BusinessException(ErrorCode.MEMBER_WITHDRAWN);
        }

        // matches(입력비밀번호, 저장된 해시값) : 저장된 해시의 솔트값을 꺼내 입력된 비밀번호와 비교
        if (request.getPassword() == null || !passwordEncoder.matches(request.getPassword(), member.PasswordHash())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        return MemberAccountResponseDTO.from(member);
    }

    // 회원 정보 수정
    @Transactional
    public MemberAccountResponseDTO updateMember(MemberUpdateRequestDTO request, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        if (request.getUpdateEmail() != null
                && !member.getEmail().equals(request.getUpdateEmail())
                && memberRepository.existsByEmail(request.getUpdateEmail())) {
            throw new BusinessException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        member.updateProfile(
                request.getUpdateEmail(),
                request.getUpdateNickname()
        );

        return MemberAccountResponseDTO.from(member);
    }

    // 회원 탈퇴
    @Transactional
    public MemberAccountResponseDTO deleteMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        member.deleteProfile(LocalDateTime.now());

        return MemberAccountResponseDTO.from(member);
    }
}
