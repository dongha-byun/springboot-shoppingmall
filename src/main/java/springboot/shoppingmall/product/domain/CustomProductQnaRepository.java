package springboot.shoppingmall.product.domain;

import java.util.List;
import springboot.shoppingmall.product.application.dto.ProductQnaDto;

public interface CustomProductQnaRepository {

    List<ProductQnaDto> findAllProductQna(Long productId);
}
