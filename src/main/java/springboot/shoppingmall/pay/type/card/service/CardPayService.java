package springboot.shoppingmall.pay.type.card.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.pay.service.PayService;
import springboot.shoppingmall.pay.type.card.web.CardApproveResponse;
import springboot.shoppingmall.pay.type.card.web.CardReadyResponse;

@Service
public class CardPayService implements PayService {

    @Override
    public Object ready(MultiValueMap<String, Object> formData) {
        String transactionId = UUID.randomUUID().toString();
        return new CardReadyResponse(
                transactionId,
                "http://localhost:3000/pay/internal/approve",
                "http://localhost:3000/pay/internal/approve",
                "http://localhost:3000/pay/internal/approve",
                LocalDateTime.now()
        );
    }

    @Override
    public Object approve(MultiValueMap<String, Object> formData) {
        return new CardApproveResponse(
                (String)formData.getFirst("cid"),
                (String)formData.getFirst("tid"),
                LocalDateTime.now()
        );
    }

    @Override
    public Object cancel(MultiValueMap<String, Object> formData) {
        return null;
    }
}
