package springboot.shoppingmall.domain.product;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import springboot.shoppingmall.domain.BaseEntity;
import springboot.shoppingmall.dto.product.ProductRequest;

@Getter
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    private double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private final List<ProductReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private final List<ProductQna> qna = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private final List<ProductDetail> details = new ArrayList<>();

    public Product(String name, int price) {
        this(null, name, price);
    }

    public Product(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.score = 0.0;
    }

    public static Product to(ProductRequest productRequest){
        return new Product(productRequest.getName(), productRequest.getPrice());
    }
}
