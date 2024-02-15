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
    public Partner findByEmailForLogin(String email) {
        return em.createQuery("select p from Partner p where p.email = :email", Partner.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
