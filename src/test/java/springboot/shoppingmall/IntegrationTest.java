package springboot.shoppingmall;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;

@Transactional
@SpringBootTest
public class IntegrationTest {

    @Autowired
    ProductRepository productRepository;

    public Product saveProduct(String name, int price, int stock, double score, int salesVolume,
                           Long categoryId, Long subCategoryId, Long partnersId, LocalDateTime now) {
        String storedFileName = "stored-file-name-" + name;
        String viewFileName = "view-file-name-" + name;
        String detail = name + "에 대한 상품 설명 입니다.";
        String productCode = "product-code-" + name;

        return productRepository.save(
                new Product(
                        name, price, stock, score, salesVolume, now,
                        categoryId, subCategoryId, partnersId,
                        storedFileName, viewFileName, detail,
                        productCode
                )
        );
    }
}
