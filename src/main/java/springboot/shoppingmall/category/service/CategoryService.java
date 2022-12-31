package springboot.shoppingmall.category.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.dto.CategoryRequest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.category.domain.CategoryRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {
        Category category = CategoryRequest.toCategory(categoryRequest);

        if (categoryRequest.getParentId() != null) {
            Category parent = findById(categoryRequest.getParentId());
            parent.addSubCategory(category);
        }

        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    public CategoryResponse findCategoryById(Long categoryId) {
        return CategoryResponse.of(findById(categoryId));
    }

    public List<CategoryResponse> findCategories() {
        return categoryRepository.findParentCategoryAll().stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    private Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("카테고리 조회 실패")
                );
    }
}
