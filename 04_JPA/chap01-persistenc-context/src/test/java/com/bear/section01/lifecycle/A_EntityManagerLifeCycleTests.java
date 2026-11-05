package com.bear.section01.lifecycle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerLifeCycleTests {

    private static EntityManagerFactory entityManagerFactory; // 싱글톤 패턴 : 한개 만들어서 써야할 때마다 재사용
    private EntityManager entityManager;

    /*
    * @BeforeAll
    * - 해당 어노테이션이 붙은 메소드는 전체 테스트가 실행되기 전 딱 한 번만 실행됨.
    * - 싱글턴 패턴을 적용하거나, 리소스가 큰 작업을 수행할 때
    * */
    @BeforeAll
    public static void initFactory() {
        /*
        * 엔티티 팩토리 매니저
        * 1. persistence.xml 파일 : 설정을 읽어 데이터베이스 연결 정보 및 엔티티 클래스 목록을 파악
        * 2. 메타 데이터 처리 : 엔티티 클래스 구조랑 관계를 정보 저장
        * 3. 커넥션 풀 초기화 : 데이터베이스 연결을 위해 내부 커넥션 풀을 설정 및 초기화
        * 4. 캐시 초기화 : 성능 최적화를 위한 (1차 캐시, 쿼리 캐시) 초기화
        * */
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    // 테스트 메서드 각각이 호출될 때 이전에 동작
    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager(); // 엔티티 매니저 생성
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close(); // 엔티티팩토리 종료
    }

    @AfterEach
    public void closeManager() {
        entityManager.close(); // 엔티티매니저 종료
    }

    /*
    * 테스트 코드는 다음과 같이 나누어 작성
    * 1. given : 테스트를 진행하기 위해 준비작업 (전제조건) 수행
    * 2. when : 테스트의 핵심로직(행동 또는 매서드) 수행
    * 3. then : 테스트의 결과를 검증
    * */
    @Test
    public void 테스트_메소드() {
    }

    @Test
    public void 엔티티매니저_팩토리_생성_확인1() {
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode());
        System.out.println("entityManager.hashCode() = " + entityManager.hashCode());
    }

    @Test
    public void 엔티티매니저_팩토리_생성_확인2() {
        System.out.println("entityManagerFactory.hashCode() = " + entityManagerFactory.hashCode()); // 1과 동일
        System.out.println("entityManager.hashCode() = " + entityManager.hashCode()); // 1과 다름
    }
}
