package org.example.jpaintro.service;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.*;
import org.example.jpaintro.entity.Category;
import org.example.jpaintro.entity.Menu;
import org.example.jpaintro.repository.CategoryRepository;
import org.example.jpaintro.repository.MenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;

    /*
     * @Transactional(readOnly = true) -> select만 허용하는 것은 아님 -> 변경이 될 수 있음
     * - 이 트랜젝션이 데이터를 변경하지 않고 조회만 한다는 의도를 Hibernate에게 알려주는 설정
     * - 클래스단에도 설정 가능함 - 클래스단에 readOnly 설정 후 메서드에 @Transactional 설정하면 가까운거를 취급하기 때문에 실행됨
     *
     * 붙였을 때의 이점
     * 1. Hibernate가 조회 결과 엔티티의 변경 감지를 위한 작업을 줄일 수 있음.
     * 2. 트랜젝션이 끝났을 때 불필요한 Flush가 발생하지 않도록 함.
     * 3. 협업에 있어서 이 메소드가 조회전용이라는 의도를 전달 가능.
     * 4. 메서드가 실행되는 동안 영속성 컨텍스트를 유지 (여러 조회도 일관된 범위로 처리 가능)
     *        -> LAZY 도 안전하게 조회됨
     *
     * 주의할 점 : select를 강제하는 기능은 아니므로 코드 작성 시 유의해야함!
     * */
    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findAll() {
        return menuRepository.findAllWithCategory().stream()
                .map(MenuResponseDTO::from)
                .toList();
    }

    @Transactional
    public MenuResponseDTO create(MenuRequestDTO request) {
        Category category = findCategory(request.getCategoryCode());
        Menu menu = new Menu(
                request.getMenuName(),
                request.getMenuPrice(),
                category,
                request.getOrderableStatus()
        );

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponseDTO.from(savedMenu);
    }

    @Transactional
    public MenuResponseDTO update(Integer menuCode, MenuRequestDTO request) {

        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "메뉴를 찾을 수 없습니다. menuCode=" + menuCode
                ));
        Category category = findCategory(request.getCategoryCode());

        menu.change(
                request.getMenuName(),
                request.getMenuPrice(),
                category,
                request.getOrderableStatus()
        );

        return MenuResponseDTO.from(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByOrderableStatus(String status) {
        List<Menu> menuList = menuRepository.findByOrderableStatusOrderByMenuPriceAscMenuCodeAsc(status);

        List<MenuResponseDTO> listMenuResponseDTO = menuList.stream()
                .map(MenuResponseDTO::from)
                .toList();

        return listMenuResponseDTO;
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByMinimumPrice(Integer price) {

        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanEqualOrderByMenuPriceAscMenuCodeAsc(price);

        return menuList.stream()
                .map(MenuResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByStatusAndMaximumPrice(String status, Integer price) {

        List<Menu> menuList = menuRepository.findByOrderableStatusAndMenuPriceLessThanEqualOrderByMenuPriceAscMenuCodeAsc(status, price);

        return menuList.stream()
                .map(MenuResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByCategoryQueryMethod(Integer categoryCode) {

        List<Menu> menuList = menuRepository.findByCategory_CategoryCodeOrderByMenuCodeAsc(categoryCode);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByCategoryNameQueryMethod(String categoryName) {

        List<Menu> menuList = menuRepository.findByCategory_CategoryNameOrderByMenuCodeAsc(categoryName);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByMenuNameContainingQueryMethod(String menuName) {

        List<Menu> menuList = menuRepository.findByMenuNameContainingOrderByMenuCodeAsc(menuName);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByMenuNameStartingWithQueryMethod(String menuName) {

        List<Menu> menuList = menuRepository.findByMenuNameStartingWithOrderByMenuCodeAsc(menuName);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByMenuNameEndingWithQueryMethod(String menuName) {

        List<Menu> menuList = menuRepository.findByMenuNameEndingWithOrderByMenuCodeAsc(menuName);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    // 클래스 안에서만 사용하는 메서드 -> 검증코드
    private Category findCategory(Integer categoryCode) {
        return categoryRepository.findById(categoryCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "카테고리를 찾을 수 없습니다. categoryCode=" + categoryCode
                ));
    }

    @Transactional(readOnly = true)
    public List<CategoryMenuStatisticsDTO> findCategoryStatistics() {
        return menuRepository.findCategoryStatistics();
    }

    @Transactional(readOnly = true)
    public List<CategoryMenuCountProjection> findCategoryMenuCounts() {
        return menuRepository.findCategoryMenuCounts();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByCategory(Integer categoryCode) {

        List<Menu> menuList = menuRepository.findByCategory(categoryCode);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> searchByPriceRange(Integer minPrice, Integer maxPrice, String status) {
        // minPrice 또는 maxPrice가 null -> 가격 범위를 올바르게 입력해야합니다.
        // minPrice < 0 / maxPrice < 0 -> 가격 범위를 올바르게 입력해야합니다.
        // minPrice > maxPrice -> 가격 범위를 올바르게 입력해야합니다.
        if (minPrice == null || maxPrice == null || minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가격 범위를 올바르게 입력해야합니다.");
        }

        // status -> Y or N
        if (!"Y".equals(status) && !"N".equals(status)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 가능 상태는 Y 또는 N 이어야 합니다.");
        }

        List<Menu> menuList = menuRepository.searchByPriceRange(minPrice, maxPrice, status);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> searchByName(String keyword) {

        List<Menu> menuList = menuRepository.searchByName(keyword);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findMenusAboveCategoryAverage() {

        List<Menu> menuList = menuRepository.findMenusAboveCategoryAverage();

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional
    public PriceUpdateResponseDTO increasePriceByCategory(Integer categoryCode, Integer increaseRate) {

        Category category = findCategory(categoryCode);
        int updatedCount = menuRepository.increasePriceByCategory(category, increaseRate);

        return new PriceUpdateResponseDTO(updatedCount);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findByCategoryNative(Integer categoryCode) {

        List<Menu> menuList = menuRepository.findByCategoryNative(categoryCode);

        return menuList.stream().map(MenuResponseDTO::from).toList();
    }
}
