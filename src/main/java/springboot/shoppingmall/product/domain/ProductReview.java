package springboot.shoppingmall.product.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.product.service.ThumbnailInfo;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "product_review")
public class ProductReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime writeDate;

    @Lob
    private String content;

    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "product_review_images",
            joinColumns = @JoinColumn(name = "review_id")
    )
    private List<ProductReviewImage> images = new ArrayList<>();

    public ProductReview(String content, int score) {
        this.writeDate = LocalDateTime.now();
        this.content = content;
        this.score = score;
    }

    public ProductReview(String content, int score, List<ProductReviewImage> images) {
        this.writeDate = LocalDateTime.now();
        this.content = content;
        this.score = score;
        this.images = images;
    }

    @Builder
    public ProductReview(String content, int score, Product product, Long userId,
                         List<ProductReviewImage> images) {
        this(content, score, images);
        byUser(userId);
        byProduct(product);
    }

    public ProductReview byUser(@NotNull Long userId) {
        this.userId = userId;
        return this;
    }

    public ProductReview byProduct(@NotNull Product product) {
        this.product = product;
        product.addReview(this);
        return this;
    }

    public String getProductName() {
        return product.getName();
    }

    public boolean isWriter(Long userId) {
        return Objects.equals(this.userId, userId);
    }
}
