package com.bear.studyboard.category.repository;

import com.bear.studyboard.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
select category
from Category category
order by category.id
""")
    List<Category> findAllWithCategory();
}
