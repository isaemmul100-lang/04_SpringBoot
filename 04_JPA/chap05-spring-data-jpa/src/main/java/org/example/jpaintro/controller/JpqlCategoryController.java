package org.example.jpaintro.controller;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.CategoryMenuCountProjection;
import org.example.jpaintro.dto.CategoryMenuStatisticsDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class JpqlCategoryController {

    private final MenuService menuService;

    /*
    * 카테고리 별 메뉴 개수와 평균 가격을 조회
    * 결과 : 카테고리 코드, 이름, 메뉴 개수, 평균 가격
    * */
    @GetMapping("/menu-statistics")
    public List<CategoryMenuStatisticsDTO> findMenuStatistics() {
        return menuService.findCategoryStatistics();
    }

    /*
    * 인터페이스를 이용한 Projection
    * 결과 : 카테고리 코드, 이름, 메뉴 개수, 평균 가격
    * */
    @GetMapping("/menu-counts")
    public List<CategoryMenuCountProjection> findMenuCounts() {
        return menuService.findCategoryMenuCounts();
    }

    @GetMapping("/categories/{categoryCode}")
    public List<MenuResponseDTO> findByCategory(@PathVariable Integer categoryCode) {
        return menuService.findByCategory(categoryCode);
    }

}
