package org.example.jpaintro.controller;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.dto.PriceIncreaseRequestDTO;
import org.example.jpaintro.dto.PriceUpdateResponseDTO;
import org.example.jpaintro.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class JpqlMenuController {

    private final MenuService menuService;

    // 전체조회
    @GetMapping
    public List<MenuResponseDTO> findAll() {

        return menuService.findAll();
    }

    /*
    * 가격 범위와 주문 가능 상태 검색
    * - 지정한 가격 범위에 포함되고 주문 가능 상태가 일치하는 메뉴 조회
    * */
    @GetMapping("/search")
    public List<MenuResponseDTO> searchByPriceRange(
            @RequestParam Integer minPrice,
            @RequestParam Integer maxPrice,
            @RequestParam String status) {
        return menuService.searchByPriceRange(minPrice, maxPrice, status);
    }

    /*
    * 메뉴이름에 요청한 검색어가 포함된 메뉴를 조회한다.
    * */
    @GetMapping("/search/name")
    public List<MenuResponseDTO> searchByName(@RequestParam String keyword) {
        return menuService.searchByName(keyword);
    }

     /*
     JPQL 상관 서브쿼리
     각 메뉴를 자신이 속한 카테고리의 평균 가격과 비교
     해당 평균보다 비싼 메뉴만 카테고리와 함께 조회
     */
    @GetMapping("/above-category-average")
    public List<MenuResponseDTO> findMenusAboveCategoryAverage() {
        return menuService.findMenusAboveCategoryAverage();
    }

    /*
    * 카테고리 별 메뉴 가격 일관 인상
     */

    /*
    * Patch VS Put
    * - Patch : 리소스 전체가 아니라 일부 속성이나 특정 상태만 변경할 때 사용 (일부분 수정)
    * - Put   : 리소스 전체를 요청 본문의 내용으로 교체할 때 사용 (전체 수정)
    *
    * 멱등성
    * - Patch -> 설계에 따라 멱등일수도 아닐 수도 있음
    * - Put   -> 요청을 여러번 보내도 일반적으로 최종상태는 같아야함.
    * */

    // 카테고리 별 메뉴 가격 일관 인상
    // 지정한 카테고리의 모든 메뉴 가격을 요청 비율만큼 한번에 인상
    // 변경된 갯수를 반환받기
    @PatchMapping("categories/{categoryCode}/price")
    public PriceUpdateResponseDTO increasePriceByCategory(
            @PathVariable Integer categoryCode,
            @RequestBody PriceIncreaseRequestDTO request
    ) {
        return menuService.increasePriceByCategory(categoryCode, request.getIncreaseRate());
    }

}
