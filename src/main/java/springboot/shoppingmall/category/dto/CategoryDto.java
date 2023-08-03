package springboot.shoppingmall.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.category.domain.Category;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryDto of(Category category) {
        return new CategoryDto(
                category.getId(), category.getName()
        );
    }
}
