package org.example.jpaintro.controller;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.jpaintro.dto.MenuRequestDTO;
import org.example.jpaintro.dto.MenuResponseDTO;
import org.example.jpaintro.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor // 초기화가 필요한 필드만 생성자로 만들어주는 어노테이션 (생성자 안 만들어도 됨)
public class MenuController {

    private final MenuService menuService;

    // 전체조회
    @GetMapping
    public List<MenuResponseDTO> findAll(ServletRequest servletRequest) {
        return menuService.findAll();
    }

    // menu 저장
    @PostMapping
    public MenuResponseDTO create(@RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.caeate(menuRequestDTO);
    }

    @PutMapping("/{menuCode}")
    public MenuResponseDTO update(@PathVariable Integer menuCode, @RequestBody MenuRequestDTO menuRequestDTO) {
        return menuService.Update(menuCode, menuRequestDTO);
    }
}
