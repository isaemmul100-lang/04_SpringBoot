package org.example.jpaintro.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Menu {

    @Id // 식별자
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto_increament로 만듦
    @Column(name = "menu_code")
    public Integer menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private Integer menuPrice;

    @Column(name = "category_code")
    private Integer categoryCode;

    @Column(name = "orderable_status")
    private String orderableStatus;

    public Menu(String menuName, Integer menuPrice, Integer categoryCode, String orderableStatus) {
        change(menuName, menuPrice, categoryCode, orderableStatus);
    }

    public void change(String menuName, Integer menuPrice, Integer categoryCode, String orderableStatus) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.categoryCode = categoryCode;
        this.orderableStatus = orderableStatus;
    }
}
