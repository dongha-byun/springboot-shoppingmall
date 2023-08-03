package springboot.shoppingmall.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.product.dto.ProductDto;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderFinder;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryFinder categoryFinder;
    private final ProductFinder productFinder;
    private final ProviderFinder providerFinder;

    @Transactional
    public ProductDto saveProduct(Long partnerId, ProductCreateDto createDto){
        ThumbnailInfo thumbnailInfo = createDto.getThumbnailInfo();
        Category category = categoryFinder.findById(createDto.getCategoryId());
        Category subCategory = categoryFinder.findById(createDto.getSubCategoryId());
        Provider provider = providerFinder.findById(partnerId);

        Product product = productRepository.save(
                Product.builder()
                        .name(createDto.getName())
                        .price(createDto.getPrice())
                        .count(createDto.getQuantity())
                        .category(category)
                        .subCategory(subCategory)
                        .partnerId(partnerId)
                        .storedFileName(thumbnailInfo.getStoredFileName())
                        .viewFileName(thumbnailInfo.getViewFileName())
                        .detail(createDto.getDetail())
                        .productCode(provider.generateProductCode())
                        .build()
        );
        return ProductDto.of(product);
    }

    public ProductResponse findProduct(Long id){
        Product product = productFinder.findProductById(id);
        Provider provider = providerFinder.findById(product.getPartnerId());
        return ProductResponse.of(product, provider);
    }
}
