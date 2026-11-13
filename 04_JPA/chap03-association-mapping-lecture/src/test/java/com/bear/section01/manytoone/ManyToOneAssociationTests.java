package com.bear.section01.manytoone;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManyToOneAssociationTests {

	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	@BeforeAll
	public static void initFactory() {
		entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
	}

	@BeforeEach
	public void initManager() {
		entityManager = entityManagerFactory.createEntityManager();
	}

	@AfterAll
	public static void closeFactory() {
		entityManagerFactory.close();
	}

	@AfterEach
	public void closeManager() {
		entityManager.close();
	}

	/*
	* 연관관계 : 엔티티 클래스간의 관계를 매핑하는 것
	* 1. 다중성에 의한 분류
	* 	- N:1(ManyToOne)
	* 	- 1:N(OneToMany)
	* 	- 1:1(OneToOne)
	* 	- N:N(ManyToMany)
	* 2. 방향에 따른 분류
	* 	- 단방향 연관관계
	* 	- 양방향 연관관계
	*
	* 여기서는 ManyToOne, 단방향
	* */
	@Test
	public void 다대일_연관관계_객체_그래프_탐색을_이용한_조회_테스트() {

		// given
		int menuCode = 15;

		// when
		/*
		* @ManyToOne의 기본 로딩 전략 EAGER
		* - find 될 때 연관된 카테고리도 로딩된다.
		* - JOIN은 보장되지 않음 (실제 SQL은 JPA 구현체, 조회방식의 차이에 따라 JOIN 또는 별도의 SELECT로 가지고 올 수도 있음)
		* */
		MenuAndCategory foundMenu = entityManager.find(MenuAndCategory.class, menuCode);
		System.out.println("조회전");
		Category menuCategory = foundMenu.getCategory();

		// then
		assertNotNull(menuCategory);
		System.out.println("foundMenu = " + foundMenu);
		System.out.println("menuCategory = " + menuCategory);
	}

	@Test
	public void 다대일_연관관계_객체지향쿼리_사용한_카테고리_이름_조회_테스트() {

		// given
		String jpql = "SELECT c.categoryName FROM menu_and_category/*엔티티명*/ m JOIN m.category c WHERE m.menuCode = 15";

		// when
		String category = entityManager.createQuery(jpql, String.class).getSingleResult();

		// then
		assertNotNull(category);
		System.out.println("category = " + category);
	}


	@Test
	public void 다대일_연관관계_객체_삽입_테스트() {

		// given
		// MenuAndCategory 엔티티
		MenuAndCategory menuAndCategory = new MenuAndCategory(); // DB에 없음
		menuAndCategory.setMenuCode(99999);
		menuAndCategory.setMenuName("죽방멸치빙수");
		menuAndCategory.setMenuPrice(30000);

		// Category 엔티티
		Category category = new Category(); // DB에 없음 -> 부모가 DB에 없음
		category.setCategoryCode(33333);
		category.setCategoryName("신규카테고리");
		category.setRefCategoryCode(null);

		menuAndCategory.setCategory(category);
		menuAndCategory.setOrderableStatus("Y");

		// when
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin(); // 트렌젝션 시작

		entityManager.persist(menuAndCategory); // 영속화

		entityTransaction.commit(); // 트렌젝션 종료

		/*
		* persist()에 전달한 Entity는 menuAndCategory 하나 뿐인데 Category까지 함께 저장됨
		* -> MenuCategory의 ManyToOne에 CascadeType.Persist 로 영속성 전파를 해놨기 때문!
		* -> 안해주면 비영속 객체 관련 예외 발생!
		* */

		// then
		MenuAndCategory foundMenuAndCategory = entityManager.find(MenuAndCategory.class, 99999);

		assertEquals(99999, foundMenuAndCategory.getMenuCode());
		assertEquals(33333, foundMenuAndCategory.getCategory().getCategoryCode());

	}

}







