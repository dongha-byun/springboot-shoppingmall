package springboot.shoppingmall.providers.domain;

public interface ProviderLoginRepository {

    Provider findByLoginId(String loginId);
}
