package com.bear.studyboard.common.error;


import lombok.*;

@Getter
@AllArgsConstructor
@ToString
public class ErrorResponseDTO {

    private String code;
    private String detail;

    static ErrorResponseDTO of(ErrorCode errorCode, String detail) {

        return new ErrorResponseDTO(
                errorCode.name(),
                detail
        );
    }

}
