package springboot.shoppingmall.order.application;

public interface OrderUserInterfaceService {

    int getOrderUserDiscountRate(Long userId);

    void increaseOrderAmounts(Long userId, int price);
}
