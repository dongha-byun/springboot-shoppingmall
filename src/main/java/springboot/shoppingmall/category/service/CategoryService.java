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
        Category parent = null;
        if(categoryRequest.getParentId() != null){
            parent = categoryRepository.findById(categoryRequest.getParentId()).orElseGet(null);
        }

        Category category = categoryRepository.save(CategoryRequest.toCategory(categoryRequest, parent));
        return CategoryResponse.of(category);
    }

    public CategoryResponse findCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("카테고리 조회 실패")
        );
        return CategoryResponse.of(category);
    }

    public List<CategoryResponse> findCategories() {
        return categoryRepository.findParentCategoryAll().stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }
}
