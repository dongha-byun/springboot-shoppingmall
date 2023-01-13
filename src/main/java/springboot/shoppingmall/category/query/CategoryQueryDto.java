package springboot.shoppingmall.category.query;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryQueryDto {
    private Long parentId;
    private String parentName;
    private Long childId;
    private String childName;

    public CategoryQueryDto(Long parentId, String parentName, Long childId, String childName) {
        this.parentId = parentId;
        this.parentName = parentName;
        this.childId = childId;
        this.childName = childName;
    }
}
