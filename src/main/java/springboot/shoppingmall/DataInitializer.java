package springboot.shoppingmall;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
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
            insertUser();
            List<Category> categories = insertCategory();
            insertProducts(categories);
        }

        private void insertProducts(List<Category> categories) {
            for (Category category : categories) {
                Long categoryId = category.getId();
                List<Category> subCategories = category.getSubCategories();
                for (Category subCategory : subCategories) {
                    Long subCategoryId = subCategory.getId();
                    for(int i=1; i<=10; i++){
                        em.persist(new Product("상품_"+categoryId+"_"+subCategoryId+"_"+i, 1234000, 321, category, subCategory));
                    }
                }
            }
        }

        private void insertUser() {
            em.persist(new User("사용자1", "user1", "a", "010-1234-1234"));
            em.persist(new User("사용자2", "user2", "a", "010-2345-2345"));
        }

        private List<Category> insertCategory() {
            Category category1 = new Category("식품");
            category1.addSubCategory(new Category("생선"));
            category1.addSubCategory(new Category("육류"));
            category1.addSubCategory(new Category("채소"));
            em.persist(category1);

            Category category2 = new Category("전자기기");
            category2.addSubCategory(new Category("카메라"));
            category2.addSubCategory(new Category("컴퓨터"));
            category2.addSubCategory(new Category("핸드폰"));
            em.persist(category2);

            Category category3 = new Category("출산/유아용품");
            category3.addSubCategory(new Category("장난감"));
            category3.addSubCategory(new Category("기타"));
            em.persist(category3);

            Category category4 = new Category("스포츠용품");
            category4.addSubCategory(new Category("야구용품"));
            category4.addSubCategory(new Category("축구용품"));
            category4.addSubCategory(new Category("배드민턴용품"));
            category4.addSubCategory(new Category("탁구/테니스용품"));
            em.persist(category4);

            Category category5 = new Category("완구/취미");
            category5.addSubCategory(new Category("보드게임"));
            category5.addSubCategory(new Category("실내대형완구"));
            category5.addSubCategory(new Category("인형"));
            category5.addSubCategory(new Category("프라모델"));
            em.persist(category5);

            Category category6 = new Category("반려동물용품");
            category6.addSubCategory(new Category("강아지사료"));
            category6.addSubCategory(new Category("강아지간식"));
            category6.addSubCategory(new Category("고양이사료"));
            category6.addSubCategory(new Category("고양이간식"));
            category6.addSubCategory(new Category("영양제"));
            category6.addSubCategory(new Category("기타용품"));
            em.persist(category6);

            Category category7 = new Category("도서/음반/DVD");
            category7.addSubCategory(new Category("소설/에세이"));
            category7.addSubCategory(new Category("자기계발/외국어"));
            category7.addSubCategory(new Category("트로트/고전음악"));
            category7.addSubCategory(new Category("대중가요/K-POP"));
            category7.addSubCategory(new Category("기타"));
            em.persist(category7);

            Category category8 = new Category("뷰티");
            category8.addSubCategory(new Category("헤어용품"));
            category8.addSubCategory(new Category("피부미용"));
            category8.addSubCategory(new Category("화장품/영양제"));
            category8.addSubCategory(new Category("기타"));
            em.persist(category8);

            Category category9 = new Category("홈인테리어");
            category9.addSubCategory(new Category("가구"));
            category9.addSubCategory(new Category("침구"));
            category9.addSubCategory(new Category("욕실용품"));
            category9.addSubCategory(new Category("기타"));
            em.persist(category9);

            Category category10 = new Category("가전디지털");
            category10.addSubCategory(new Category("핸드폰"));
            category10.addSubCategory(new Category("컴퓨터/노트북"));
            category10.addSubCategory(new Category("태블릿 PC"));
            category10.addSubCategory(new Category("기타"));
            em.persist(category10);

            return Arrays.asList(
                    category1, category2, category3, category4, category5,
                    category6, category7, category8, category9, category10
            );
        }
    }
}
