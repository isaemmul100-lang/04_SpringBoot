package org.example.jpaintro.repository;

import org.example.jpaintro.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JpaRepository<엔티티 타입, 식별자 타입>
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query(value = "SELECT m FROM Menu m WHERE m.menuName = :menuName")
    public List<Menu> findMenusByCategoryName(@Param("menuName")String menuName);
}
