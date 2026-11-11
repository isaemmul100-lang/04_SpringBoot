package com.bear.studyboard.member.controller;

import com.bear.studyboard.common.dto.ApiResponse;
import com.bear.studyboard.member.dto.MemberAccountResponseDTO;
import com.bear.studyboard.member.dto.MemberUpdateRequestDTO;
import com.bear.studyboard.member.dto.RegistryMemberRequestDTO;
import com.bear.studyboard.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> register(
            @RequestBody RegistryMemberRequestDTO request
    ) {

        MemberAccountResponseDTO member = memberService.register(request);

        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status)
                .body(ApiResponse.of(status, "회원등록성공", member));
    }

    // 회원 정보 수정
    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> updateMember(
            @RequestBody MemberUpdateRequestDTO request,
            @PathVariable Long memberId
    ) {

        MemberAccountResponseDTO member = memberService.updateMember(request, memberId);

        HttpStatus status = HttpStatus.OK;

        return ResponseEntity.status(status)
                .body(ApiResponse.of(status, "회원정보 수정 성공", member));
    }

    // 회원 탈퇴
    @PostMapping("/{memberId}/withdrawal")
    public ResponseEntity<ApiResponse<MemberAccountResponseDTO>> deleteMember(
            @PathVariable Long memberId
    ) {

        MemberAccountResponseDTO member = memberService.deleteMember(memberId);

        HttpStatus status = HttpStatus.OK;

        return ResponseEntity.status(status)
                .body(ApiResponse.of(status, "회원탈퇴 성공", member));
    }
}
