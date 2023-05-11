package springboot.shoppingmall.order.partners.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

/**
 * 판매자가 준비중 인 주문을 조회하면 준비중/출고중인 주문 내역을 조회한다.
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersReadyOrderQueryDto {
    private Long id;
    private String orderCode;           // 주문코
    private LocalDateTime orderDate;    // 주문일자
    private String productCode;         // 상품코드
    private String productName;         // 상품명
    private int quantity;               // 구매수량
    private int totalPrice;             // 결제금액
    private String userName;            // 주문자(구매자)
    private String telNo;               // 연락처
    private String receiverName;        // 수령인
    private String address;             // 배송지 주소
    private String detailAddress;       // 배송지 상세주소
    private String requestMessage;      // 배송요청사항
    private OrderStatus orderStatus;    // 주문상태
    private String invoiceNumber;       // 송장번호
}
