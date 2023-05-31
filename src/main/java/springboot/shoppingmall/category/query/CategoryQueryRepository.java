package springboot.shoppingmall.category.query;

import java.util.List;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.dto.CategoryDto;
import springboot.shoppingmall.category.dto.CategoryResponse;

public interface CategoryQueryRepository {

    List<CategoryQueryDto> findCategoryAll();

    List<CategoryDto> findParentsDto();
    List<CategoryDto> findSubCategoryDto(List<Long> parentIds);
}
