package org.example.jpaintro.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PracticeCategoryMenuStatisticsDTO {

    private Integer categoryCode;
    private String categoryName;
    private Integer minPrice;
    private Integer maxPrice;
}
