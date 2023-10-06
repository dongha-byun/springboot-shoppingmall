package springboot.shoppingmall.coupon.application.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ResponseUserInformation implements Serializable {
    private Long userId;
    private String userName;
    private String grade;
}
