package com.seatup.category.service;

import com.seatup.category.dto.CategoryListResponse;
import com.seatup.category.entity.Category;
import com.seatup.category.repository.CategoryRepository;
import com.seatup.category.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 공연 분류 리스트를 반환한다.
     * @return
     */
    public List<CategoryListResponse> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryListResponse::from)
                .toList();

    }

    /**
     * 아이디에 해당하는 카테고리를 조회해 반환한다.
     * @param categoryId
     * @return
     */
    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }
}
