package springboot.shoppingmall.partners.domain;

public interface PartnerLoginRepository {

    Partner findByEmailForLogin(String email);
}
