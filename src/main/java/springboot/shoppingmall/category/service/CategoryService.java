package springboot.shoppingmall.category.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.dto.CategoryRequest;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.category.query.CategoryQueryDto;
import springboot.shoppingmall.category.query.CategoryQueryRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

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
        List<CategoryQueryDto> categories = categoryQueryRepository.findCategoryAll();
        return categories.stream()
                .collect(
                        groupingBy(categoryQueryDto -> new CategoryResponse(categoryQueryDto.getParentId(),
                                        categoryQueryDto.getParentName()),
                                mapping(categoryQueryDto -> new CategoryResponse(categoryQueryDto.getChildId(),
                                        categoryQueryDto.getChildName()), toList()
                                )
                        )).entrySet().stream()
                .map(
                        e -> new CategoryResponse(e.getKey().getId(), e.getKey().getName(), e.getValue())
                ).collect(toList());
    }

    private Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("카테고리 조회 실패")
                );
    }
}
