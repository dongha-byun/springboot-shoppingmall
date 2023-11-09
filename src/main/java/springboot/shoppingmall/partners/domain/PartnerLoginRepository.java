package springboot.shoppingmall.partners.domain;

public interface PartnerLoginRepository {

    Partner findByLoginId(String loginId);
}
