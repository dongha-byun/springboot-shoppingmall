package springboot.shoppingmall.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductQna;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ProductQnaResponse {
    private Long id;
    private String content;
    private String writerName;

    public static ProductQnaResponse of(ProductQna productQna) {
        return new ProductQnaResponse(productQna.getId(), productQna.getContent(),
                productQna.getWriter().getUserName());
    }
}
