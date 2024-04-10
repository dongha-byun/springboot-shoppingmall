package springboot.shoppingmall.client.payservice;

public interface PayServiceClient {

    void cancel(Long orderId, int cancelAmount);
}
