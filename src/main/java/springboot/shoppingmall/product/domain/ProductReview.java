package springboot.shoppingmall.product.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
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

    @Column(name = "user_id")
    private Long userId;

    public ProductReview(String content, int score) {
        this.writeDate = LocalDateTime.now();
        this.content = content;
        this.score = score;
    }

    @Builder
    public ProductReview(String content, int score, Product product, Long userId) {
        this(content, score);
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
}
