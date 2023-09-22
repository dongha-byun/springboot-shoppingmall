package springboot.shoppingmall.product.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ThumbnailInfo {
    private String storedFileName;
    private String viewFileName;
}
