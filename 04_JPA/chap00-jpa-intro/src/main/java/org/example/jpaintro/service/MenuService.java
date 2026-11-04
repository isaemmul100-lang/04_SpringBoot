package org.example.jpaintro.service;

import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.MenuRequestDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.entity.Menu;
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

    public List<MenuResponseDTO> findAll() {

        // 데이터 베이스에서 조회가 됨
        List<Menu> menuList = menuRepository.findAll(); // 여기에 있는 findAll()은 만든적이 없는데 왜 있을까?

        List<MenuResponseDTO> list = menuList.stream()
                .map(menu -> MenuResponseDTO.from(menu))
                .toList();

        return list;
    }

    @Transactional
    public MenuResponseDTO caeate(MenuRequestDTO menuRequestDTO) {
        Menu menu = new Menu(
                menuRequestDTO.getMenuName(),
                menuRequestDTO.getMenuPrice(),
                menuRequestDTO.getCategoryCode(),
                menuRequestDTO.getOrderableStatus()
        );

        // db에 저장
        Menu savedMenu = menuRepository.save(menu);

        return MenuResponseDTO.from(savedMenu);
    }

    @Transactional
    public MenuResponseDTO Update(Integer menuCode, MenuRequestDTO menuRequestDTO) {

        // id로 수정할 메뉴 정보 찾아오기
        Menu menu = menuRepository.findById(menuCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다.")); // 없으면 예외를 던지고 있으면 menu에 담겠다는 것임
        // 값 변경
        menu.change(
                menuRequestDTO.getMenuName(),
                menuRequestDTO.getMenuPrice(),
                menuRequestDTO.getCategoryCode(),
                menuRequestDTO.getOrderableStatus()
        );

        return MenuResponseDTO.from(menu);
    }
}
