package springboot.shoppingmall;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.user.domain.User;

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

            Category category1 = new Category("식품", null);
            em.persist(category1);
            em.persist(new Category("생선", category1));
            em.persist(new Category("육류", category1));
            em.persist(new Category("채소", category1));

            Category category2 = new Category("전자기기", null);
            em.persist(category2);
            em.persist(new Category("카메라", category2));
            em.persist(new Category("컴퓨터", category2));
            em.persist(new Category("핸드폰", category2));

            Category category3 = new Category("출산/유아용품", null);
            em.persist(category3);
            em.persist(new Category("장난감", category3));
            em.persist(new Category("기타", category3));

            Category category4 = new Category("스포츠용품", null);
            em.persist(category4);
            em.persist(new Category("야구용품", category4));
            em.persist(new Category("축구용품", category4));
            em.persist(new Category("배드민턴용품", category4));
            em.persist(new Category("탁구/테니스용품", category4));

            Category category5 = new Category("완구/취미", null);
            em.persist(category5);
            em.persist(new Category("보드게임", category5));
            em.persist(new Category("실내대형완구", category5));
            em.persist(new Category("인형", category5));
            em.persist(new Category("프라모델", category5));

            Category category6 = new Category("반려동물용품", null);
            em.persist(category6);
            em.persist(new Category("강아지사료", category6));
            em.persist(new Category("강아지간식", category6));
            em.persist(new Category("고양이사료", category6));
            em.persist(new Category("고양이간식", category6));
            em.persist(new Category("영양제", category6));
            em.persist(new Category("기타용품", category6));

            Category category7 = new Category("도서/음반/DVD", null);
            em.persist(category7);
            em.persist(new Category("소설/에세이", category7));
            em.persist(new Category("자기계발/외국어", category7));
            em.persist(new Category("트로트/고전음악", category7));
            em.persist(new Category("대중가요/K-POP", category7));
            em.persist(new Category("기타", category7));

            Category category8 = new Category("뷰티", null);
            em.persist(category8);
            em.persist(new Category("헤어용품", category8));
            em.persist(new Category("피부미용", category8));
            em.persist(new Category("화장품/영양제", category8));
            em.persist(new Category("기타", category8));

            Category category9 = new Category("홈인테리어", null);
            em.persist(category9);
            em.persist(new Category("가구", category9));
            em.persist(new Category("침구", category9));
            em.persist(new Category("욕실용품", category9));
            em.persist(new Category("기타", category9));

            Category category10 = new Category("가전디지털", null);
            em.persist(category10);
            em.persist(new Category("핸드폰", category10));
            em.persist(new Category("컴퓨터/노트북", category10));
            em.persist(new Category("태블릿PC", category10));
            em.persist(new Category("기타", category10));
        }
    }
}
