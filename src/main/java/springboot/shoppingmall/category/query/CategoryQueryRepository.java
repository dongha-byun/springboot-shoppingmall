package springboot.shoppingmall.category.query;

import java.util.List;
import springboot.shoppingmall.category.dto.CategoryResponse;

public interface CategoryQueryRepository {

    List<CategoryQueryDto> findCategoryAll();
}
