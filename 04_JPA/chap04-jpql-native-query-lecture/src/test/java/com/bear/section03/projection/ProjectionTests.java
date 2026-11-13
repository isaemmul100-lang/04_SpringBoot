package com.bear.section03.projection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProjectionTests {

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
    * Projection (프로젝션) : SELECT 절에 조회할 대상을 지정하는 것
    * ex) (SELECT {프로젝션 대상} FROM)
    *
    * 프로젝션 종류
    * 1. 엔티티 프로젝션
    *   - 원하는 객체를 바로 조회할 수 있고 조회된 엔티티는 영속성 컨텍스트가 관리
    * 2. 임베디드 타입 프로젝션
    *   - 엔티티와 거의 동일. 대신 조회의 시작점이 될 수 없음 (From에 못 쓰임)
    * 3. 스칼라 타입 프로젝션
    *   - 숫자, 문자, 날짜 같은 기본 데이터 타입 (영속성 컨텍스트에서 관리 안됨)
    * 4. new 명령어를 이용한 프로젝션
    *   - 다양한 종류의 단순 값을 DTO로 바로 조회하는 방식 (영속성 컨텍스트에서 관리 안됨)
    * */

    @Test
    public void 단일_엔티티_프로젝션_테스트() {

        // when
        String jpql = "SELECT m FROM menu_section03 m";
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();

        // then
        assertNotNull(menuList);

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        menuList.get(1).setMenuName("test");

        entityTransaction.commit();

    }

    @Test
    public void 양방향_연관관계_엔티티_프로젝션_테스트() {

        // given
        int menuCodeParameter = 3;

        // when
        String jpql = "SELECT m.category FROM bidirection_menu m WHERE m.menuCode = :menuCode";
        BiDirectionCategory categoryOfMenu = entityManager.createQuery(jpql, BiDirectionCategory.class)
                .setParameter("menuCode", menuCodeParameter)
                .getSingleResult();

        // then
        assertNotNull(categoryOfMenu);
        System.out.println(categoryOfMenu);

        assertNotNull(categoryOfMenu.getMenuList());
        categoryOfMenu.getMenuList().forEach(System.out::println);

    }


    @Test
    public void TypedQuery를_이용한_스칼라_타입_프로젝션_테스트() {

        // when
        String jpql = "SELECT c.categoryName FROM category_section03 c";
        List<String> categoryNameList = entityManager.createQuery(jpql, String.class).getResultList();

        // then
        assertNotNull(categoryNameList);
        categoryNameList.forEach(System.out::println);

    }

    @Test
    public void Query를_이용한_스칼라_타입_프로젝션_테스트() { // 영속 X

        /*
        * 조회하려는 컬럼이 1개일 경우에는 TypedQuery로 반환타입을 명시할 수 있지만
        * 2개 이상일 경우에는 명시하지 못하므로 Object[] 형태로 데이터를 받아서 형변환 해서 사용한다.
        * */

        // when
        String jpql = "SELECT c.categoryCode, c.categoryName FROM category_section03 c";
        List<Object[]> categoryList = entityManager.createQuery(jpql).getResultList();

        // then
        assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });

    }

    @Test
    public void new_명령어를_활용한_프로젝션_테스트() {

        /*
        * 조회하려는 칼럼이 2개 이상일 경우에
        * 조회하려는 컬럼의 값을 스칼라 값으로 받아 별도의 클래스를 만들고,
        * new 객체(스칼라, 스칼라) 형태로 객체를 생성하여 사용할 수 있다.
        * */

        // when
        String jpql = "SELECT new com.bear.section03.projection.CategoryInfo(c.categoryCode, c.categoryName) FROM category_section03 c";
        List<CategoryInfo> categoryInfoList = entityManager.createQuery(jpql, CategoryInfo.class).getResultList();

        // then
        assertNotNull(categoryInfoList);
        categoryInfoList.forEach(System.out::println);
    }
}
