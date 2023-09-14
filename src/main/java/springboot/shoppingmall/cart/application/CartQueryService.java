package springboot.shoppingmall.cart.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.cart.domain.CartQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartQueryService {
    private final CartQueryRepository cartQueryRepository;

    public List<CartQueryDto> findAllByUser(Long userId) {
        return cartQueryRepository.findAllCartByUserId(userId);
    }
}
