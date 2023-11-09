package springboot.shoppingmall;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.UsingDuration;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.delivery.domain.Delivery;

//@Component
@RequiredArgsConstructor
@Profile("local")
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
            List<Partner> partners = insertPartners();
            List<Long> userIds = insertUser();
            List<Category> categories = insertCategory();

            insertDelivery(userIds);
            insertProducts(categories, partners);
            insertCoupon(partners, userIds);
        }

        private void insertDelivery(List<Long> userIds) {
            userIds.forEach(
                    userId -> {
                        Delivery delivery = Delivery.builder()
                                .nickName("집")
                                .receiverName("테스터" + userId)
                                .receiverPhoneNumber("010-2222-333"+userId)
                                .zipCode("07122")
                                .address("서울시 영등포구 당산동")
                                .detailAddress("102호")
                                .requestMessage("무인 택배함에 넣어주세요.")
                                .userId(userId)
                                .build();
                        em.persist(delivery);
                    }
            );
        }

        private List<Partner> insertPartners() {
            Partner partner1 = new Partner(
                    "부실건설", "변부실", "서울시 영등포구", "02-1234-2222"
                    , "110-33-444222", "test1", "test1!"
            );
            Partner partner2 = new Partner(
                    "파산은행", "김파산", "부산광역시 사상구", "051-333-2222"
                    , "251-89-698111", "test2", "test2@"
            );

            partner1.approve();
            partner2.approve();

            em.persist(partner1);
            em.persist(partner2);

            return Arrays.asList(partner1, partner2);
        }


        private void insertProducts(List<Category> categories, List<Partner> partners) {
            for (Category category : categories) {
                Long categoryId = category.getId();
                List<Category> subCategories = category.getSubCategories();
                for (Category subCategory : subCategories) {
                    Long subCategoryId = subCategory.getId();
                    for(int i=1; i<=100; i++){
                        Partner partner = partners.get(i%2);
                        em.persist(new Product("상품_"+categoryId+"_"+subCategoryId+"_"+i, 190 * i,
                                501-i, category, subCategory, partner.getId(),
                                null, null, i + "번째 상품 설명 입니다.",
                                partner.generateProductCode()));
                    }
                }
            }
        }

        private List<Long> insertUser() {
            return Arrays.asList(1L, 2L, 3L);
        }

        private void insertCoupon(List<Partner> partners, List<Long> userIds) {
            Coupon coupon1OfPartners1 = new Coupon(
                    "할인쿠폰 #1",
                    new UsingDuration(
                            LocalDateTime.of(2023, 3, 5, 0, 0, 0),
                            LocalDateTime.of(2023, 11, 1, 23, 59, 59)
                    ), 5, partners.get(0).getId()
            );
            Coupon coupon2OfPartners1 = new Coupon(
                    "할인쿠폰 #2",
                    new UsingDuration(
                            LocalDateTime.of(2023, 6, 5, 0, 0, 0),
                            LocalDateTime.of(2023, 9, 1, 23, 59, 59)
                    ), 8, partners.get(0).getId()
            );
            Coupon coupon1OfPartners2 = new Coupon(
                    "입점 기념 할인쿠폰 #1",
                    new UsingDuration(
                            LocalDateTime.of(2023, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2023, 6, 30, 23, 59, 59)
                    ), 10, partners.get(1).getId()
            );
            Coupon coupon2OfPartners2 = new Coupon(
                    "입점 기념 할인쿠폰 #2",
                    new UsingDuration(
                            LocalDateTime.of(2023, 7, 1, 0, 0, 0),
                            LocalDateTime.of(2024, 12, 31, 23, 59, 59)
                    ), 7, partners.get(1).getId()
            );
            userIds.forEach(
                    userId -> {
                        coupon1OfPartners1.addUserCoupon(userId);
                        coupon2OfPartners1.addUserCoupon(userId);
                        coupon1OfPartners2.addUserCoupon(userId);
                        coupon2OfPartners2.addUserCoupon(userId);
                    }
            );

            em.persist(coupon1OfPartners1);
            em.persist(coupon2OfPartners1);
            em.persist(coupon1OfPartners2);
            em.persist(coupon2OfPartners2);
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
