package com.bear.section02.parameter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParameterBindingTests {

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
    * 파라미터 바인딩
    * 1. 이름 기준 바인딩
    *   - ':' 다음에 이름 기준 파라미터를 정한다.
    * 2. 위치 기준 바인딩
    *   - '?' 다음에 값을 주고 위치값은 1부터 시작한다.
    * */

    @Test
    public void 이름_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {

        // given
        String menuNameParameter = "한우딸기국밥";

        // when
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = :menuName";

        /*
        밑에 코드랑 동일함
        TypedQuery<Menu> query = entityManager.createQuery(jpql, Menu.class);
        TypedQuery<Menu> query2 = query.setParameter("menuName", menuNameParameter);
        List<Menu> menuList2 = query2.getResultList();
        */

        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter("menuName", menuNameParameter) // setParameter(쿼리에 콜론 붙여논 변수명, 넣고싶은 값)
                .getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

    @Test
    public void 위치_기준_파라미터_바인딩_메뉴_목록_조회_테스트() {

        // given
        String menuNameParameter = "한우딸기국밥";

        // when
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = ?1";

        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setParameter(1, menuNameParameter)
                .getResultList();

        // then
        assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
