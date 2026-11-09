package org.example.jpaintro.repository;

import org.example.jpaintro.dto.CategoryMenuCountProjection;
import org.example.jpaintro.dto.CategoryMenuStatisticsDTO;
import org.example.jpaintro.entity.Category;
import org.example.jpaintro.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("select menu from Menu menu join fetch menu.category")
    List<Menu> findAllWithCategory();

    /*
     * Spring Data JPA의 쿼리메서드
     * - 정해진 규칙에 따라 메서드 이름을 작성하면 Spring Data JPA가 JPQL을 자동 생성함
     *
     * - findBy + 조건 필드 + 비교키워드 + OrderBy + 정렬필드 + Asc/Desc
     * 키워드
     * - And / Or : 조건 연결
     * - Containing : 문자열 포함검색
     * - GreaterThenEqual / LessTanEqual : 이상 / 이하
     * - Between : 범위 검색
     * */
    List<Menu> findByOrderableStatusOrderByMenuPriceAscMenuCodeAsc(String orderableStatus);

    List<Menu> findByMenuPriceGreaterThanEqualOrderByMenuPriceAscMenuCodeAsc(Integer price);

    List<Menu> findByOrderableStatusAndMenuPriceLessThanEqualOrderByMenuPriceAscMenuCodeAsc(String orderableStatus, Integer menuPrice);

    // _ : 연관관계의 경계를 명확하게 표현하기 위해 쓰는 의미
    List<Menu> findByCategory_CategoryCodeOrderByMenuCodeAsc(Integer categoryCategoryCode);

    List<Menu> findByCategory_CategoryNameOrderByMenuCodeAsc(String categoryCategoryName);

    /*
     * - Containing : SQL의 LIKE %검색어%
     * - StartingWith : 시작하는 단어
     * - EndingWith : 끝나는 단어
     * */
    List<Menu> findByMenuNameContainingOrderByMenuCodeAsc(String menuName);

    List<Menu> findByMenuNameStartingWithOrderByMenuCodeAsc(String menuName);

    List<Menu> findByMenuNameEndingWithOrderByMenuCodeAsc(String menuName);

    /*
     * DTO Projection
     * - JPQL에서 new 키워드와 DTO의 전체 패키지 이름을 사용
     *
     * 주의점
     * - JPQL에 DTO에 전체 패키지 이름을 작성
     * - select 항목의 순서와 DTO 생성자 매개변수 순서가 같아야함.
     * - count 결과는 Long, avg 결과는 Double
     * - 생성자 필요
     * */
    @Query("""
            select new org.example.jpaintro.dto.CategoryMenuStatisticsDTO(
                        category.categoryCode,
                        category.categoryName,
                        count(menu),
                        avg(menu.menuPrice)
                        )
            from Menu menu
            join menu.category category
            group by category.categoryCode, category.categoryName
            order by category.categoryCode asc
            """)
    List<CategoryMenuStatisticsDTO> findCategoryStatistics();

    @Query("""
            select 
                  category.categoryCode as categoryCode,
                  category.categoryName as categoryName,
                  count(menu) as menuCount
            from Menu menu
            join menu.category category
            group by category.categoryCode, category.categoryName
            order by category.categoryCode asc
            """)
    List<CategoryMenuCountProjection> findCategoryMenuCounts();

    // List<Menu> findByCategory_CategoryCodeOrderByMenuCodeAsc(Integer categoryCategoryCode); 이 코드랑 출력결과가 같은 코드임
    @Query("""
            select menu
            from Menu menu
            join menu.category
            where menu.category.categoryCode = :categoryCode
            order by menu.menuName asc
            """)
    List<Menu> findByCategory(Integer categoryCode);

    /*
     * @Param : JPQL 파라미터와 Java 매개변수의 연결을 명확하게 하기 위해 사용 (생략 가능)
     * */
    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.menuPrice between :minPrice and :maxPrice
                and menu.orderableStatus = :status
            order by menu.menuPrice asc, menu.menuCode asc
            """)
    List<Menu> searchByPriceRange(
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("status") String status);

    // List<Menu> findByMenuNameContainingOrderByMenuCodeAsc(String menuName); 출력결과가 같은 코드
    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.menuName like concat('%', :keyword,'%')
            order by menu.menuCode asc
            """)
    List<Menu> searchByName(@Param("keyword") String keyword);

    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.menuPrice > (
                        select  avg(subMenu.menuPrice)
                        from Menu subMenu
                        where subMenu.category = menu.category
                        )
            order by menu.category.categoryCode asc
            """)
    List<Menu> findMenusAboveCategoryAverage();

    /*
    * @Modifying
    * - @Query : 기본적으로 조회용 select 쿼리로 처리됨
    * - @Query에 작성된 쿼리가 조회가 아니라 데이터를 변경하는 I, U, D 성격의 쿼리라는 사실을 JPA에게 알려줌
    * - JPQL 또는 네이티브 쿼리로 Update, Delete를 실행할 때 사용
    * */

    /*
    * flushAutomatically : 벌크쿼리를 실행하기 전에 영속성 컨텍스트의 변경 내용을 DB에 Flush
    * flushAutomatically : 벌크쿼리를 실행한 후에, 영속성 컨텍스트를 자동으로 초기화
    * */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            update Menu menu
            set menu.menuPrice = menu.menuPrice + (menu.menuPrice * :increaseRate / 100)
            where menu.category = :category
            """)
    int increasePriceByCategory(@Param("category") Category category, @Param("increaseRate") Integer increaseRate);

    /*
    * 네이티브 쿼리보다 JPQL이 권장되는 이유
    * 1. DB 종류에 덜 의존함 -> JPQL로 작성하면 DB를 종류를 변경했을 때 자동으로 바꿔줌
    * 2. JPA 기능과 자연스럽게 연결됨 -> 엔티티 기준으로 사용 가능
    * 3. 네이티브쿼리 -> 데이터베이스 테이블명, 컬럼명에 의존하지만, JPQL 엔티티구조로 관리 가능
    * */
    // nativeQuery = true : 일반 DB 쿼리를 쓸 수 있게 해줌
    @Query(value = """
            select menu.*
            from tbl_menu menu
            where menu.category_code = :categoryCode
            order by menu.menu_code asc
            """, nativeQuery = true)
    List<Menu> findByCategoryNative(Integer categoryCode);
}
