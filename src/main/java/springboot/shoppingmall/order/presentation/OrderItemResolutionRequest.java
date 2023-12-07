package springboot.shoppingmall.order.presentation;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemResolutionRequest {

    @NotBlank(message = "사유는 필수항목 입니다.")
    private String content;
}
