package com.bear.studyboard.category.service;

import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {

        return categoryRepository.findAllWithCategory();

    }
}
