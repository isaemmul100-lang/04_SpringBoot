package org.example.restapi.section02.commonresponse;

import lombok.*;

/*
* 모든 API에서 공통으로 사용할 응답 형식
*
* @param <R> 응답 본문에 담을 데이터 타입
* */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApiResponse<T> {

    private int status;
    private String message;
    private T data;

    /*
    * 팩토리 메서드
    * - 객체 생성 과정을 객체를 생성, 반환하는 메서드
    * - 생성 목적을 이름으로 명확하게 표현
    *
    * ApiResponse.success(...) 로 호출
    * */

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> failure(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}
