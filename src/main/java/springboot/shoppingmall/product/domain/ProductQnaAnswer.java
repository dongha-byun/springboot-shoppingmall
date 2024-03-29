package springboot.shoppingmall.product.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_qna_answer")
public class ProductQnaAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String answer;

    private LocalDateTime answerDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_qna_id")
    private ProductQna productQna;

    public static ProductQnaAnswer createQnaAnswer(String answer, ProductQna qna){
        ProductQnaAnswer productQnaAnswer = new ProductQnaAnswer();
        productQnaAnswer.answer = answer;
        productQnaAnswer.answerDate = LocalDateTime.now();

        productQnaAnswer.ofQna(qna);

        return productQnaAnswer;
    }

    private ProductQnaAnswer ofQna(ProductQna qna){
        this.productQna = qna;
        qna.addAnswer(this);

        return this;
    }
}
