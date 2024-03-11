package springboot.shoppingmall.product.query.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.product.query.ProductQueryOrderType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductQueryRequestParam {
    private Long categoryId;
    private Long subCategoryId;
    private ProductQueryOrderType orderType = ProductQueryOrderType.SCORE;
    private int limit = 1;
    private int offset = 10;
}
