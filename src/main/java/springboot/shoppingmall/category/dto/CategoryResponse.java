package springboot.shoppingmall.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.category.domain.Category;

@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CategoryResponse {

    private Long id;
    private String name;
    private List<CategoryResponse> subCategories;

    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryResponse(Long id, String name, List<CategoryResponse> subCategories) {
        this.id = id;
        this.name = name;
        if(!subCategories.isEmpty()) {
            this.subCategories = subCategories;
        }
    }

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getName(),
                category.getSubCategories().stream()
                        .map(CategoryResponse::of)
                        .collect(Collectors.toList()));
    }

    public static CategoryResponse of(CategoryDto categoryDto, List<CategoryDto> subCategoriesDto) {
        return new CategoryResponse(categoryDto.getId(), categoryDto.getName(),
                subCategoriesDto.stream()
                        .map(subCategoryDto -> new CategoryResponse(subCategoryDto.getId(), subCategoryDto.getName()))
                        .collect(Collectors.toList()));
    }

    public List<CategoryResponse> getSubCategories() {
        return subCategories;
    }
}
