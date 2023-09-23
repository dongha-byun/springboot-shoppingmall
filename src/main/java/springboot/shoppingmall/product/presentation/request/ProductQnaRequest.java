package springboot.shoppingmall.product.presentation.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.dto.ProductQnaCreateDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaRequest {
    @NotBlank(message = "내용은 필수항목 입니다.")
    private String content;

    public ProductQnaCreateDto toDto() {
        return new ProductQnaCreateDto(content);
    }
}
