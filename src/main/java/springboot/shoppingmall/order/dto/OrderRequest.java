package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String tid;
    private Long productId;
    private int quantity;
    private int deliveryFee;
    private String receiverName;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private int totalPrice;
}
