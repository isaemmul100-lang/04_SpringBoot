package com.bear.section01.manytoone;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "menu_and_category")
@Table(name = "tbl_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuAndCategory {

    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    @JoinColumn(name = "category_code",
            nullable = true, // NUL 허용 여부
            unique = false, // 외래키의 UNIQUE 제약조건
            insertable = true, // 외래키 컬럼은 SQL INSERT에 포함할지
            updatable = true // 외래키 컬럼은 SQL Update에 포함할지
    )
    /*
    * 매핑 어노테이션 설정
    * - fetch : 연관 엔티티 조회 전략 지정 (Eager(즉시로딩-참조하는 엔티티가 있으면 그 테이블도 함께 조회), Lazy(지연로딩-필요한 시점에 조회))
    * - optional : 연관 엔티티가 없어도 되는지 지정 (default ture)
    * - cascade : 현재 엔티티의 영속성 작업을 연관 엔티티에도 전파할지 설정
    *   - PERSIST : 현재 엔티티를 저장할 때 연관 엔티티도 같이 저장
    *   - MERGE : 현재 엔티티를 병합할 때 연관 엔티티도 병합
    *   - REMOVE : 현재 엔티티를 삭제할 때 연관 엔티티도 삭제
    *   - REFRESH : 현재 엔티티를 새로고침(다시조회)할 때 연관 엔티티도 새로고침
    *   - ALL : 모두 적용
    * */
//    @ManyToOne // 기본설정 : fetch : Eager, cascade : 없음, optional : True
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) // PERSIST : 하면 카테고리도 insert 가능하게 됨
    private Category category;

    @Column(name = "orderable_status")
    private String orderableStatus;

}
