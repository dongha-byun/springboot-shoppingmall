package springboot.shoppingmall.product.query.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;
import springboot.shoppingmall.product.query.repository.PartnersProductQnaRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartnerProductQnaQueryService {

    private final PartnersProductQnaRepository partnersProductQnaRepository;

    public List<PartnersProductQnaDto> findPartnersProductQna(Long partnerId,
                                                              ProductQnaAnswerCompleteType completeType) {
        return partnersProductQnaRepository.findPartnersProductQnaAll(
                partnerId, completeType);
    }
}
