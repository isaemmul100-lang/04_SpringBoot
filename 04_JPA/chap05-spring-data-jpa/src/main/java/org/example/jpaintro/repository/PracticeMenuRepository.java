package org.example.jpaintro.repository;

import org.example.jpaintro.dto.CategoryMenuCountProjection;
import org.example.jpaintro.dto.CategoryMenuStatisticsDTO;
import org.example.jpaintro.dto.PracticeCategoryMenuStatisticsDTO;
import org.example.jpaintro.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PracticeMenuRepository extends JpaRepository<Menu, Integer> {

    @Query("""
            select menu
            from Menu menu
            where menu.menuPrice between :minPrice and :maxPrice
            order by menu.menuPrice desc, menu.menuCode desc
            """)
    List<Menu> findMenusByPriceRange(Integer minPrice, Integer maxPrice);

    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.category.categoryCode = :categoryCode
                        and menu.orderableStatus = :status
            """)
    List<Menu> findMenusByCategoryAndStatus(
            @Param("categoryCode") Integer categoryCode,
            @Param("status") String status);

    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.orderableStatus = "Y"
            order by menu.menuPrice asc
            """)
    List<Menu> findOrderableMenus();

    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.category.categoryCode = :categoryCode
            and menu.menuPrice <= :maxPrice
            """)
    List<Menu> searchMenus(
            @Param("categoryCode") Integer categoryCode,
            @Param("maxPrice") Integer maxPrice);

    // 별칭 필수 인터페이스랑 이름 맞춰야함
    @Query("""
            select 
                category.categoryCode as categoryCode,
                category.categoryName as categoryName,
                avg(menu.menuPrice) as menuCount
            from Menu menu
            join menu.category category
            group by category.categoryCode, category.categoryName
            order by category.categoryCode
            """)
    List<CategoryMenuCountProjection> findOrderableMenuCounts();

    @Query("""
            select new org.example.jpaintro.dto.PracticeCategoryMenuStatisticsDTO(
                category.categoryCode,
                category.categoryName,
                min(menu.menuPrice),
                max(menu.menuPrice)
            )
            from Menu menu
            join menu.category category
            group by category.categoryCode, category.categoryName
            """)
    List<PracticeCategoryMenuStatisticsDTO> findCategoryPriceStatistics();

    @Query("""
            select menu
            from Menu menu
            join fetch menu.category
            where menu.menuPrice < (
                select avg(submenu.menuPrice)
                from Menu submenu
            )
            order by menu.menuPrice asc, menu.menuCode asc
            """)
    List<Menu> findMenusBelowAverage();

    @Query(value = """
select *
from tbl_menu menu
where menu_price >= :price
order by menu_price desc
""", nativeQuery = true)
    List<Menu> findExpensiveMenusNative(Integer price);
}
