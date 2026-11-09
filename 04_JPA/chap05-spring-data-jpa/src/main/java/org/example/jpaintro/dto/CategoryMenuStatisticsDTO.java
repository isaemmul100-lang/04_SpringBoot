package org.example.jpaintro.dto;

import lombok.*;

/*
* 카테고리 별 메뉴 통계 조회 결과를 받는 DTO Projection
*
* Projection
* - 쿼리 결과를 엔티티 전체로 받지 않고 조회에 필요한 값만 원하는 형태로 선택해서 받는 조회전용 객체
* - DB 테이블에 저장되는 객체가 아닌, 엔티티처럼 영속성 컨텍스트에서 관리되지 않음
*
* 동작방식
* - 엔티티 전체를 조회하고 JPQL에서 필요한 값만 선택하여 DTO 생성자를 호출함
* */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMenuStatisticsDTO {

    private Integer categoryCode;
    private String categoryName;
    private Long menuCount;
    private Double averagePrice;
}
