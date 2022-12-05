package springboot.shoppingmall.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.product.Category;
import springboot.shoppingmall.dto.category.CategoryRequest;
import springboot.shoppingmall.dto.category.CategoryResponse;
import springboot.shoppingmall.repository.category.CategoryRepository;

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
}
