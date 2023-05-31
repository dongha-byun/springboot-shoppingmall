package springboot.shoppingmall.category.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryFinder {
    private final CategoryRepository categoryRepository;

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
                );
    }
}
