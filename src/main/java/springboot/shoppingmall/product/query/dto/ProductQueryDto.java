package springboot.shoppingmall.product.query.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductQueryDto {
    private Long id;
    private String name;
    private int price;
    private int stock;
    private double score;
    private int salesVolume;
    private LocalDateTime registerDate;
    private String storedThumbnailName;
    private String viewThumbnailName;
    private Long partnersId;
    private String partnersName;
}
