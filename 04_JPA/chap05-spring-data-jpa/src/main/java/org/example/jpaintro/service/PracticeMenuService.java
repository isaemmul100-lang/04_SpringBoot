package org.example.jpaintro.service;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.CategoryMenuCountProjection;
import org.example.jpaintro.dto.CategoryMenuStatisticsDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.dto.PracticeCategoryMenuStatisticsDTO;
import org.example.jpaintro.entity.Menu;
import org.example.jpaintro.repository.CategoryRepository;
import org.example.jpaintro.repository.PracticeMenuRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PracticeMenuService {

    private final PracticeMenuRepository practiceMenuRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findMenusByPriceRange(Integer minPrice, Integer maxPrice) {

        if (minPrice == null || maxPrice == null || minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가격 범위를 올바르게 입력해야합니다.");
        }

        List<Menu> menuList = practiceMenuRepository.findMenusByPriceRange(minPrice, maxPrice);
        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findMenusByCategoryAndStatus(Integer categoryCode, String status) {

        if (categoryCode == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리에 값을 넣어야합니다.");
        }

        List<Menu> menuList = practiceMenuRepository.findMenusByCategoryAndStatus(categoryCode, status);
        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findOrderableMenus() {
        List<Menu> menuList = practiceMenuRepository.findOrderableMenus();
        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> searchMenus(Integer categoryCode, Integer maxPrice) {
        if (categoryCode == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "카테고리에 값을 넣어야합니다.");
        }
        if (maxPrice == null || maxPrice < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가격 범위를 올바르게 입력해야합니다.");
        }

        List<Menu> menuList = practiceMenuRepository.searchMenus(categoryCode, maxPrice);
        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryMenuCountProjection> findOrderableMenuCounts() {
        return practiceMenuRepository.findOrderableMenuCounts();
    }

    @Transactional(readOnly = true)
    public List<PracticeCategoryMenuStatisticsDTO> findCategoryPriceStatistics() {
        return practiceMenuRepository.findCategoryPriceStatistics();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findMenusBelowAverage() {
        List<Menu> menuList = practiceMenuRepository.findMenusBelowAverage();
        return menuList.stream().map(MenuResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDTO> findExpensiveMenusNative(Integer price) {
        List<Menu> menuList = practiceMenuRepository.findExpensiveMenusNative(price);
        return menuList.stream().map(MenuResponseDTO::from).toList();
    }
}
