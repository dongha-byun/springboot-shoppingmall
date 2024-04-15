package springboot.shoppingmall.pay.type.kakakopay.service;

import springboot.shoppingmall.order.application.dto.OrderCreateDto;

public interface OrderClient {

    void order(Long userId, OrderCreateDto orderCreateDto);
}
