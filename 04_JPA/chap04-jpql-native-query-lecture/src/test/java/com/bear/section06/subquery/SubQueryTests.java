package com.bear.section06.subquery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubQueryTests {
	
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
	* 조건절에 쓰는 서브쿼리 -> Having, Where
	* From절에 쓰는 서브쿼리 -> 파생 테이블
	* */

	@Test
	public void 서브쿼리를_이용한_메뉴_조회_테스트() {
		
		// given
		String categoryNameParameter = "한식";
		
		// when
		String jpql = "SELECT m " +
						"FROM menu_section06 m " +
						"WHERE m.categoryCode = (SELECT c.categoryCode " +
											"FROM category_section06 c " +
													"WHERE c.categoryName = :categoryName)";
		List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
				.setParameter("categoryName", categoryNameParameter)
				.getResultList();
		
		// then
		assertNotNull(menuList);
		menuList.forEach(System.out::println);
		
	}
}
