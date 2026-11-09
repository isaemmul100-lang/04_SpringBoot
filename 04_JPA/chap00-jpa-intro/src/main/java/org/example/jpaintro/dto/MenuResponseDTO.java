package org.example.jpaintro.dto;

import lombok.*;
import org.example.jpaintro.entity.Menu;

// 응답 전용 DTO
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuResponseDTO {

    public Integer menuCode;
    private String menuName;
    private Integer menuPrice;
    private Integer categoryCode;
    private String categoryName;
    private String orderableStatus;

    // Menu 클래스를 MenuResponseDTO로 반환시키는 팩토리 메소드
    public static MenuResponseDTO from(Menu menu) {
        return new MenuResponseDTO(
                menu.getMenuCode(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getCategoryCode().getCategoryCode(),
                menu.getCategoryCode().getCategoryName(),
                menu.getOrderableStatus()
        );
    }
}
