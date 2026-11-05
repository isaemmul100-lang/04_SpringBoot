package com.bear.section03.persistance;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class A_EntityLifeCycleTest {

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
    public void 준영속성_detach_테스트() {

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        System.out.println("foundMenu1 = " + foundMenu1);
        System.out.println("foundMenu2 = " + foundMenu2);

        // 준영속 상태 : 컨텍스트로 관리하던 엔티티 객체를 관리하지 않는 상태가 되면 준영속 상태라고 함.
        entityManager.detach(foundMenu2); // foundMenu2 준영속 상태로 변경

        foundMenu1.setMenuPrice(5000); // 영송상태에서의 변경

        foundMenu2.setMenuPrice(5000); // 준영속상태에서의 변경

        // 영속성 컨텍스트에서 변경된 내용을 가지고 옴
        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());

        // 영속상태가 아니기 때문에 변경이 되지 않아 기대한 값을 가져오지 못함
        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 준영속성_clear_테스트() {

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        entityManager.clear(); // 둘 다 영속상태 해제 (둘 다 준영속으로 만듦)

        foundMenu1.setMenuPrice(5000);

        foundMenu2.setMenuPrice(5000);

        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());

        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    public void 준영속성_close_테스트() {

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // 영속성 컨텍스트를 닫는다. (재사용 불가)
        entityManager.close();

        foundMenu1.setMenuPrice(5000); // close를 해도 준영속이 됨

        foundMenu2.setMenuPrice(5000);

        assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());

        assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    /*
    * merge() : 
    * - 준영속 엔티티 객체의 식별자 값으로 1차 엔티티 객체를 조회
    * - 조회한 엔티티 객체의 준영속 상태의 엔티티 객체의 값을 병합해서 엔티티 객체를 반환
    * -> 영속성 컨텍스트의 관리를 받지 않는 엔티티 값을 영속성 컨텍스트에 관리되는 엔티티에 복사하는 기능
    * */
    @Test
    public void 병합_merge_수정_테스트() {

        Menu menuToDetach = entityManager.find(Menu.class, 2); // 영속 상태
        entityManager.detach(menuToDetach); // 준영속 (영속 X)

        menuToDetach.setMenuName("수박죽"); // 바꿔줬을 때 -> 반영 안됨

        Menu refoundMenu = entityManager.find(Menu.class, 2); // 영속상태
        
        System.out.println(menuToDetach.hashCode());
        System.out.println(refoundMenu.hashCode());

        entityManager.merge(menuToDetach); // 준영속 객체의 값을 영속객체로 옮기는 행위

        // 캐싱된 영속 객체가 반환 됨!
        Menu mergedMenu = entityManager.find(Menu.class, 2);
        // 트랜잭션이 걸려있어야 DB까지 바뀜 하지만 지금은 트랜잭션이 안 걸려있어서 아직 캐시 된 객체가 반환됐을 뿐 DB에 반영은 전이라 패스한 것임
        assertEquals("수박죽", mergedMenu.getMenuName());

    }

    @Test
    public void 병합_merge_삽입_테스트() {

        Menu menuToDetach = entityManager.find(Menu.class, 2); // 영속 상태
        entityManager.detach(menuToDetach); // 준영속 상태

        menuToDetach.setMenuCode(999); // 데이터 변경(ID)
        menuToDetach.setMenuName("수박죽"); // 데이터 변경

        // 있으면 병합, 없으면 새로 만듦
        entityManager.merge(menuToDetach);

        // 새로운 데이터를 조회
        Menu mergedMenu = entityManager.find(Menu.class, 999);
        assertEquals("수박죽", mergedMenu.getMenuName());

    }
}
