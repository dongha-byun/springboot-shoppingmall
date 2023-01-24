package springboot.shoppingmall.product.domain;

import static javax.persistence.FetchType.*;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "product_qna")
public class ProductQna extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime writeDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @OneToOne(mappedBy = "productQna", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductQnaAnswer answer;

    @Builder
    public ProductQna(String content, Product product, User writer) {
        this(null, content, product, writer);
    }

    public ProductQna(Long id, String content, Product product, User writer) {
        this.id = id;
        this.content = content;
        this.writeDate = LocalDateTime.now();
        this.writer = writer;
        this.product = product;
        if(product != null){
            product.addQna(this);
        }
    }

    public void addAnswer(ProductQnaAnswer answer){
        this.answer = answer;
    }
}
