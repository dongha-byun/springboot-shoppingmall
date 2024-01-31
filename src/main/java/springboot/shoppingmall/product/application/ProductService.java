package springboot.shoppingmall.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.product.application.dto.ProductCreateDto;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.application.dto.ProductDto;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.partners.domain.PartnerFinder;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryFinder categoryFinder;
    private final PartnerFinder partnerFinder;

    public ProductDto saveProduct(Long partnerId, ProductCreateDto createDto){
        ThumbnailInfo thumbnailInfo = createDto.getThumbnailInfo();
        Category category = categoryFinder.findById(createDto.getCategoryId());
        Category subCategory = categoryFinder.findById(createDto.getSubCategoryId());
        Partner partner = partnerFinder.findById(partnerId);

        Product product = productRepository.save(
                Product.builder()
                        .name(createDto.getName())
                        .price(createDto.getPrice())
                        .stock(createDto.getStock())
                        .category(category)
                        .subCategory(subCategory)
                        .partnerId(partnerId)
                        .storedFileName(thumbnailInfo.getStoredFileName())
                        .viewFileName(thumbnailInfo.getViewFileName())
                        .detail(createDto.getDetail())
                        .productCode(partner.generateProductCode())
                        .build()
        );
        return ProductDto.of(product);
    }
}
