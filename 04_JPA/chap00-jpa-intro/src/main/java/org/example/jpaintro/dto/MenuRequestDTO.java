package org.example.jpaintro.dto;

import lombok.*;

// 요청 전용 DTO
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuRequestDTO {

    private String menuName;
    private Integer menuPrice;
    private Integer categoryCode;
    private String orderableStatus;
}
