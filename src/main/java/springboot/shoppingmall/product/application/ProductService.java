package springboot.shoppingmall.product.application;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.file.util.FileHandler;
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
    private final FileHandler fileHandler;

    public ProductDto saveProduct(Long partnerId, ProductCreateDto createDto){
        ThumbnailInfo thumbnailInfo = createDto.getThumbnailInfo();
        Category category = categoryFinder.findById(createDto.getCategoryId());
        Category subCategory = categoryFinder.findById(createDto.getSubCategoryId());
        Partner partner = partnerFinder.findById(partnerId);

        // 정규식 으로 개선하면 좋은데 말이지
        String oldDetail = createDto.getDetail();
        String newDetail = oldDetail.replaceAll("/content/img/temp/", "/content/img/prod/");
        createDto.setDetail(newDetail);

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

        // file handler 를 직접 부를지, 비동기로 할지 고민
        String tempContentImageURL = "http://localhost:8000/content/img/temp/";
        String regex = "<img[^>]*src=[\"']?"+tempContentImageURL+"([^>\"']+)[\"']?[^>]*>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(oldDetail);
        List<String> storeFileNames = new ArrayList<>();
        while(matcher.find()) {
            storeFileNames.add(matcher.group(1));
        }
        fileHandler.copyContentImageTempToProd(storeFileNames);

        return ProductDto.of(product);
    }

}
