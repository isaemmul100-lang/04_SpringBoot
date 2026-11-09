package org.example.jpaintro.controller;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.MenuRequestDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus/query-method")
@RequiredArgsConstructor
public class QueryMethodMenuController {

    private final MenuService menuService;

    // menu 저장
    @PostMapping
    public MenuResponseDTO create(@RequestBody MenuRequestDTO menuRequestDTO) {

        return menuService.create(menuRequestDTO);
    }

    // 메뉴 수정
    @PutMapping("/{menuCode}")
    public MenuResponseDTO update(@PathVariable Integer menuCode, @RequestBody MenuRequestDTO menuRequestDTO) {

        return menuService.update(menuCode,menuRequestDTO);
    }

    /*
    * 요청한 주문 가능 상태와 일치하는 메뉴를 조회
    * - 가격이 앉은 메뉴부터 반환, 가격이 같으면 메뉴 코드순으로 응답
    * */
    @GetMapping("/orderable")
    public List<MenuResponseDTO> findByOrderableStatus(@RequestParam String status) {
        return menuService.findByOrderableStatus(status);
    }

    /*
    * - 요청 가격 이상인 메뉴 조회
    * - 가격이 낮은 메뉴부터 반환, 가격이 같으면 메뉴 코드순으로 반환
    * */
    @GetMapping("/price")
    public List<MenuResponseDTO> findByMinimumPrice(@RequestParam Integer price) {
        return menuService.findByMinimumPrice(price);
    }

    // 여러 조건을 결합
    /*
     * - 주문 가능 상태가 요청 상태와 같고 요청 가격 이하인 메뉴를 조회
     * - 두 조건을 만족하는 메뉴만 가격 오름차순으로 정렬
     * */
    @GetMapping("/search")
    public List<MenuResponseDTO> findByStatusAndMaximumPrice(
            @RequestParam Integer price,
            @RequestParam String status) {
        return menuService.findByStatusAndMaximumPrice(status, price);
    }

    // 연관관계
    /*
    * - 요청한 카테고리에 속한 메뉴를 조회
    * - 조회 결과는 메뉴 코드 오름차순
    * */
    @GetMapping("/categories/{categoryCode}")
    public List<MenuResponseDTO> findByCategory(@RequestParam Integer categoryCode) {
        return menuService.findByCategoryQueryMethod(categoryCode);
    }

    // FK가 아닌 카테고리 이름으로 조회
    @GetMapping("/category-name")
    public List<MenuResponseDTO> findByCategory_name(@RequestParam String categoryName) {
        return menuService.findByCategoryNameQueryMethod(categoryName);
    }

    /*
    * 메뉴 이름에 요청한 검색어가 포함된 메뉴를 조회
    * - Containing : SQL의 LIKE %검색어%
    * - StartingWith : 시작하는 단어
    * - EndingWith : 끝나는 단어
    * */
    @GetMapping("/nemuname-containing")
    public List<MenuResponseDTO> findByMenuNameContaining(@RequestParam String menuName) {
        return menuService.findByMenuNameContainingQueryMethod(menuName);
    }

    @GetMapping("/nemuname-startingwith")
    public List<MenuResponseDTO> findByMenuNameStartingWith(@RequestParam String menuName) {
        return menuService.findByMenuNameStartingWithQueryMethod(menuName);
    }

    @GetMapping("/nemuname-endingwith")
    public List<MenuResponseDTO> findByMenuNameEndingWith(@RequestParam String menuName) {
        return menuService.findByMenuNameEndingWithQueryMethod(menuName);
    }
}
