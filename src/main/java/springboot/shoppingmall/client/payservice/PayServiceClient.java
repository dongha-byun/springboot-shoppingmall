package springboot.shoppingmall.client.payservice;

public interface PayServiceClient {

    void cancel(String tid, String payType, int cancelAmount);
}
