package com.bear.section07.namedquery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import jakarta.persistence.*;

@Entity(name="menu_section07")
@Table(name="tbl_menu")
// 데이터베이스의 데이터들을 활용하기 위해 집계함수를 쓰거나 미리 연산이 되어야할 때
@NamedQueries({
	@NamedQuery(name="menu_section07.selectMenuList", query="SELECT m FROM menu_section07 m") // 미리 지정한 정적 쿼리
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu {

	@Id
	@Column(name="menu_code")
	private int menuCode;

	@Column(name="menu_name")
	private String menuName;

	@Column(name="menu_price")
	private int menuPrice;

	@Column(name="category_code")
	private int categoryCode;

	@Column(name="orderable_status")
	private String orderableStatus;
}
