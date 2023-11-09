package springboot.shoppingmall.partners.domain;

import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PartnerLoginRepositoryImpl implements PartnerLoginRepository {

    private final EntityManager em;

    public PartnerLoginRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Partner findByLoginId(String loginId) {
        return em.createQuery("select p from Partner p where p.loginId = :loginId", Partner.class)
                .setParameter("loginId", loginId)
                .getSingleResult();
    }
}
