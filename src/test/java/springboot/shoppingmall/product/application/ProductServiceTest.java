package springboot.shoppingmall.product.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.application.dto.ProductCreateDto;
import springboot.shoppingmall.product.application.dto.ProductDto;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;

@Transactional
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PartnerRepository partnerRepository;

    Category category;
    Category subCategory;
    Partner partner;

    @BeforeEach
    void beforeEach(){
        category = categoryRepository.save(new Category("의류"));
        subCategory = categoryRepository.save(new Category("바지").changeParent(category));
        partner = partnerRepository.save(
                new Partner(
                        "테스트판매처", "테스트대표님", "테스트시 테스트구 테스트동",
                        "031-222-4411", "304-22-31422",
                        "test_provider", "test_provider1!", true)
        );
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void save_product() {
        // give
        ProductCreateDto productCreateDto = new ProductCreateDto(
                "청바지", 20000, 100, "상품 설명입니다.",
                category.getId(), subCategory.getId(),
                new ThumbnailInfo("저장 시 적용될 파일명", "화면에 보여질 파일명(원래 파일명)")
        );

        // when
        ProductDto productDto = productService.saveProduct(partner.getId(), productCreateDto);

        // then
        assertThat(productDto.getId()).isNotNull();
    }
}
