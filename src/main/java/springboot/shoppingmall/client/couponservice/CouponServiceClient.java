package springboot.shoppingmall.client.couponservice;

import org.springframework.scheduling.annotation.Async;

public interface CouponServiceClient {

    @Async
    void recoveryCoupon(Long userCouponId);
}
