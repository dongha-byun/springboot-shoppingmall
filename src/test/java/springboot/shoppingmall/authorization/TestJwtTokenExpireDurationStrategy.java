package springboot.shoppingmall.authorization;

import springboot.shoppingmall.authorization.service.JwtTokenExpireDurationStrategy;

public class TestJwtTokenExpireDurationStrategy implements JwtTokenExpireDurationStrategy {
    @Override
    public long getAccessTokenExpireDuration() {
        return 1000L;
    }

    @Override
    public long getRefreshTokenExpireDuration() {
        return 1000L;
    }
}
