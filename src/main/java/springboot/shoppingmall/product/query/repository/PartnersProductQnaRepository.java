package springboot.shoppingmall.product.query.repository;

import java.util.List;
import springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;

public interface PartnersProductQnaRepository {
    List<PartnersProductQnaDto> findPartnersProductQnaAll(Long partnerId, ProductQnaAnswerCompleteType completeType);
}
