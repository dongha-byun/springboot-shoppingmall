package springboot.shoppingmall.providers.domain;

import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderLoginRepositoryImpl implements ProviderLoginRepository{

    private final EntityManager em;

    public ProviderLoginRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Provider findByLoginId(String loginId) {
        return em.createQuery("select p from Provider p where p.loginId = :loginId", Provider.class)
                .setParameter("loginId", loginId)
                .getSingleResult();
    }
}
