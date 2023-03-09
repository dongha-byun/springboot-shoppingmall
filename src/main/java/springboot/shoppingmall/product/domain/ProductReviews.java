package springboot.shoppingmall.product.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class ProductReviews {

    @OneToMany(mappedBy = "product")
    private List<ProductReview> reviews = new ArrayList<>();

    public void addReview(ProductReview review) {
        this.reviews.add(review);
    }

    public void removeReview(ProductReview review) {
        this.reviews.remove(review);
    }

    public int getCount() {
        return reviews.size();
    }

    public double getAverageScore() {
        int totalScore = 0;
        for (ProductReview review : reviews) {
            totalScore += review.getScore();
        }
        return Math.round((((double)totalScore / getCount()) * 10)) / 10.0;
    }

    public List<ProductReview> getReviews() {
        return this.reviews;
    }
}
