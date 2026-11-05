package com.bear.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class A_EntityManagerCRUDTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");}

    @BeforeEach
    public void initManager() {entityManager = entityManagerFactory.createEntityManager();}

    @AfterAll
    public static void closeFactory() {entityManagerFactory.close();}

    @AfterEach
    public void closeManager() {entityManager.close();}

    @Test
    public void 새로운_메뉴_추가_테스트() {

        // given
        Menu menu = new Menu();
        menu.setMenuName("롤백 시키기 위한 데이터");
        menu.setMenuPrice(5500);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction(); // entityManager에서 트랜젝션객체 받기
        entityTransaction.begin(); // 트랜잭션 시작

        try {
            /*
            * persist()는 엔티티를 영속성 컨텍스트에 등록만 하고, insert 구문은 commit 시점까지 쓰기 지연 저장소에 모았다가 한번에 내보낸다.
            * 그런데
            * @GeneratedValue(strategy = GenerationType.IDENTITY) 전략에 의해 컨텍스트에 저장할 ID값이 필요하므로
            * 미리 알기 위해 insert가 바로 실행된다.
            * */
            entityManager.persist(menu); // 만든 객체를 영속성 컨텍스트에 등록 (영속성 컨텍스트에 등록하기 위해 미리 sql을 보낸다.

            entityTransaction.commit(); // DB에 반영
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }

        // contains(menu) : 해당 객체가 persistance context에 등록이 되어있는지 확인하는 메서드
        // then
        assertTrue(entityManager.contains(menu)); // 있으면 true 반환
    }

    @Test
    public void 메뉴코드로_메뉴_조회_테스트() {

        int menuCode = 2;

        // when
        // find(엔티티 클래스, 식별자) : 식별자로 엔티티 하나 조회
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        Assertions.assertNotNull(foundMenu); // null이 아니면 true

        // 찾아온 메뉴 데이터의 menuCode가 2인지?
        assertEquals(menuCode, foundMenu.getMenuCode());
        System.out.println("foundMenu = " + foundMenu);

    }

    @Test
    public void 메뉴_이름_수정_테스트() {

        Menu menuToChange = entityManager.find(Menu.class, 2);
        System.out.println("menu = " + menuToChange);

        String menuNameToChange = "갈치스무디";

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            /*
            * 변경 감지 (dirty checking)
            * - 영속 상태의 엔티티는 값만 바구면 update()나 save() 하지 않아도
            * - 최초 상태의 스냅샷을 기억했다가 commit 시점에 달라진 값으로 데이터베이스에 적용할 update 구문을 작성해준다.
            * */
            menuToChange.setMenuName(menuNameToChange);

            entityTransaction.commit(); // 더티체킹
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }

        assertEquals(menuNameToChange, entityManager.find(Menu.class, 2).getMenuName()); // 테스트 성공

    }

    @Test
    public void 메뉴_삭제하기_테스트() {

        Menu menuToRemove = entityManager.find(Menu.class, 1); // 영속성 컨텍스트에 menuToRemove 객체 등록

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin(); // 트랜잭션 시작

        try {

            entityManager.remove(menuToRemove); // 영속상태를 해제 (삭제)
            entityTransaction.commit(); // 데이터베이스에서 제거 구문 작성 및 전송
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }

        Menu removedMenu = entityManager.find(Menu.class, 1);
        assertNull(removedMenu); // 위의 값이 null값인가? -> 테스트 성공
    }
}
