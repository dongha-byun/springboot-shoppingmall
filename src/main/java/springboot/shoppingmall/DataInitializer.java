package springboot.shoppingmall;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.user.User;

@Profile("local")
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.init();
    }

    @Component
    static class InitService {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init(){

            em.persist(new User("사용자1", "user1", "a", "010-1234-1234"));
            em.persist(new User("사용자2", "user2", "a", "010-2345-2345"));

        }
    }
}
