package springboot.shoppingmall.product.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.order.exception.OverQuantityException;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    private int stock;

    private double score;

    private int salesVolume;

    private LocalDateTime registerDate;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Column(name = "partner_id")
    private Long partnerId;

    @Embedded
    private ProductThumbnail thumbnail;

    @Embedded
    private final ProductReviews reviews = new ProductReviews();

    @Lob
    private String detail;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductQna> qna = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductDetail> details = new ArrayList<>();

    @Builder
    public Product(String name, int price, int stock, Long categoryId, Long subCategoryId
            , Long partnerId, String storedFileName, String viewFileName, String detail, String productCode) {
        this(null, name, price, stock, 0.0, 0, LocalDateTime.now(), categoryId, subCategoryId,
                partnerId, storedFileName, viewFileName, detail, productCode);
    }

    public Product(String name, int price, int stock, double score, int salesVolume,
                   LocalDateTime registerDate, Long categoryId, Long subCategoryId, Long partnerId,
                   String storedFileName, String viewFileName, String detail, String productCode) {
        this(null, name, price, stock, score, salesVolume, registerDate, categoryId, subCategoryId,
                partnerId, storedFileName, viewFileName, detail, productCode);
    }

    public Product(Long id, String name, int price, int stock, double score, int salesVolume,
                   LocalDateTime registerDate, Long categoryId, Long subCategoryId, Long partnerId,
                   String storedFileName, String viewFileName, String detail, String productCode) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.score = score;
        this.salesVolume = salesVolume;
        this.registerDate = registerDate;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.partnerId = partnerId;
        this.thumbnail = new ProductThumbnail(storedFileName, viewFileName);
        this.productCode = productCode;
        this.detail = detail;
    }

    public void addQna(ProductQna productQna){
        this.getQna().add(productQna);
    }

    public ProductQna findQna(Long qnaId) {
        return qna.stream().filter(
                        qna -> qna.getId().equals(qnaId)
                )
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException("문의 글이 존재하지 않습니다.")
                );
    }

    public void increaseSalesVolume(int salesCount){
        this.salesVolume += salesCount;
    }

    public void addReview(ProductReview productReview) {
        this.reviews.addReview(productReview);
    }

    public void removeReview(ProductReview productReview) {
        this.reviews.removeReview(productReview);
    }

    public void refreshScore() {
        this.score = reviews.getAverageScore();
    }

    public String getThumbnail() {
        if(this.thumbnail == null){
            return "";
        }
        return this.thumbnail.getStoredFileName();
    }

    // 상품의 재고 수를 감소시킨다.
    public void removeQuantity(int stock) {
        this.stock -= stock;
    }


    public void increaseQuantity(int stock) {
        this.stock += stock;
    }

    public void validateQuantity(int quantity) {
        if(this.stock < quantity) {
            throw new OverQuantityException(this.name + " 상품의 재고수량이 초과하여 주문이 불가합니다.");
        }
    }
}
