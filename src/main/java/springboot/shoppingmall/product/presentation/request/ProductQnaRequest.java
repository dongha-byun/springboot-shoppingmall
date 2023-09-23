package springboot.shoppingmall.product.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.dto.ProductQnaCreateDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaRequest {
    private String content;

    public ProductQnaCreateDto toDto() {
        return new ProductQnaCreateDto(content);
    }
}
