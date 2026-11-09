package org.example.jpaintro.service;

import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.entity.Category;
import org.example.jpaintro.entity.Menu;
import org.example.jpaintro.repository.CategoryRepository;
import org.example.jpaintro.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*
 * MenuService의 비즈니스 로직을 검증하는 단위 테스트
 * Mockito로 만든 가짜 Repository를 사용
 *
 * 실제 Repository를 사용하지 않고 테스트에 필요한 동작만 설정할 수 있음
 * */
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock // Mockito가 가짜 객체를 생성한다.
    private MenuRepository menuRepository;

    @Mock //
    private CategoryRepository categoryRepository;

    // 테스트 대상인 MenuService 객체를 생성
    // Mock으로 만든 가짜객체를 생성자로 주입해줌
    @InjectMocks
    private MenuService menuService;

    @Test
    void 최저가격이_null이면_예외가_발생한다() {
        // given
        Integer minPrice = null;
        Integer maxPrice = 10000;
        String status = "Y";

        // when
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> menuService.searchByPriceRange(minPrice, maxPrice, status)
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        // Mock 메서드가 호출되었는지를 확인
        /*
         * never()는 호출 횟수가 0번 이어야함
         * any()는 매개변수 값과 관계없이 모든 값을 허용
         * */
        verify(menuRepository, never()).searchByPriceRange(any(), any(), any());

    }

    @Test
    void 최고가격이_null이면_예외가_발생한다() {
        // given
        Integer minPrice = 1000;
        Integer maxPrice = null;
        String status = "Y";

        // when
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> menuService.searchByPriceRange(minPrice, maxPrice, status)
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        // Mock 메서드가 호출되었는지를 확인
        /*
         * never()는 호출 횟수가 0번 이어야함
         * any()는 매개변수 값과 관계없이 모든 값을 허용
         * */
        verify(menuRepository, never()).searchByPriceRange(any(), any(), any());

    }

    @Test
    void 주문가능상태가_잘못되면_예외가_발생한다() {
        // given
        Integer minPrice = 1000;
        Integer maxPrice = 10000;
        String status = "A";

        // when
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> menuService.searchByPriceRange(minPrice, maxPrice, status)
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(menuRepository, never()).searchByPriceRange(any(), any(), any());
    }

    @Test
    void 주문가능상태가_Y이면_메뉴를_DTO로_반환한다() {
        // given
        Integer minPrice = 1000;
        Integer maxPrice = 10000;
        String status = "Y";

        // 테스트에 필요한 가짜 Category
        Category category = mock(Category.class);

        // when().thenReturn() getter 호출 시 반환할 값을 미리 설정
        when(category.getCategoryCode()).thenReturn(4);
        when(category.getCategoryName()).thenReturn("한식");

        // repository에서 찾은 menu 라고 가정
        Menu menu = new Menu("테스트메뉴", 5000, category, status);

        // when().thenReturn() repository.searchByPriceRange 호출 시 반환할 값을 미리 설정
        when(menuRepository.searchByPriceRange(minPrice, maxPrice, status))
                .thenReturn(List.of(menu));

        // when
        List<MenuResponseDTO> result = menuService.searchByPriceRange(minPrice, maxPrice, status);

        // then
        assertEquals("테스트메뉴", result.get(0).getMenuName());
        assertEquals(4, result.get(0).getCategoryCode());
        assertEquals("한식", result.get(0).getCategoryName());

        // searchByPriceRange가 한 번 호출되었는지 확인
        verify(menuRepository).searchByPriceRange(minPrice, maxPrice, status);
    }
}