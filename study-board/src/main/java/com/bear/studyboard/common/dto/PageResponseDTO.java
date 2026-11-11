package com.bear.studyboard.common.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResponseDTO<T> {

    private List<T> items;
    private int page;
    private int size;
    private long totalElements;

    /*
    * Page -> pageable, sort 정보 등도 포함하고 있음
    * 이 내용들이 출력되지 않도록 클라이언트에서 필요한 내용들만 PageResponseDTO로 분리해서 사용
    * */
    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }
}
