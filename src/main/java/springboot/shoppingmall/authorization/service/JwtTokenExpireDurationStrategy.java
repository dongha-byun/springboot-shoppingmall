package springboot.shoppingmall.authorization.service;

public interface JwtTokenExpireDurationStrategy {
    long getAccessTokenExpireDuration();
    long getRefreshTokenExpireDuration();
}
