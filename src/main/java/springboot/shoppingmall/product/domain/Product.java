package springboot.shoppingmall.product.domain;

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
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.BaseEntity;

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

    private int count;

    private double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private Category subCategory;

    @OneToMany(mappedBy = "product")
    private final List<ProductReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private final List<ProductQna> qna = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private final List<ProductDetail> details = new ArrayList<>();

    public Product(String name, int price, int count, Category category, Category subCategory) {
        this(null, name, price, count, 0.0, category, subCategory);
    }

    public Product(Long id, String name, int price, int count, double score, Category category, Category subCategory) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.score = score;
        this.category = category;
        this.subCategory = subCategory;
    }
}
