package springboot.shoppingmall.product.domain;

import static javax.persistence.FetchType.*;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.user.domain.User;

@Entity
@Getter
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

    private boolean isResponse;

    private LocalDateTime responseDate;

    @Lob
    private String responseContent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User writer;
}
