package springboot.shoppingmall.pay.type.kakakopay.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.coupon.domain.OrderCodeCreator;
import springboot.shoppingmall.order.application.dto.OrderCreateDto;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.service.KakaoPayService;
import springboot.shoppingmall.pay.type.kakakopay.web.approve.KakaoPayApproveRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.approve.KakaoPayApproveResponse;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelResponse;
import springboot.shoppingmall.pay.type.kakakopay.web.ready.KakaoPayReadyRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.ready.KakaoPayReadyResponse;
import springboot.shoppingmall.pay.web.request.PayApproveRequest;
import springboot.shoppingmall.pay.web.request.PayRequest;

@RequiredArgsConstructor
@RestController
public class KakaoPayController {
    private final KakaoPayService kakaoPayService;
    private final OrderCodeCreator orderCodeCreator;

    @PostMapping("/pay/KAKAO_PAY/ready")
    public ResponseEntity<KakaoPayReadyResponse> readyPay(@GatewayAuthentication GatewayAuthInfo authInfo,
                                                          @RequestBody PayRequest<KakaoPayReadyRequest> param) {
        KakaoPayReadyRequest readyRequest = param.getData();
        String orderCode = orderCodeCreator.createOrderCode();
        KakaoPayReadyRequestDto readyRequestDto = readyRequest.toDto(
                orderCode, String.valueOf(authInfo.getUserId())
        );

        KakaoPayReadyResponseDto readyResponseDto = kakaoPayService.ready(readyRequestDto);
        KakaoPayReadyResponse responseBody = KakaoPayReadyResponse.of(
                readyResponseDto, orderCode
        );

        return ResponseEntity.ok().body(responseBody);
    }
    @PostMapping("/pay/KAKAO_PAY/approve")
    public ResponseEntity<KakaoPayApproveResponse> approvePay(@GatewayAuthentication GatewayAuthInfo authInfo,
                                                              @RequestBody PayApproveRequest<KakaoPayApproveRequest> param) {
        KakaoPayApproveRequest approveRequest = param.getData();
        KakaoPayApproveRequestDto approveRequestDto = approveRequest.toDto(
                String.valueOf(authInfo.getUserId())
        );

        OrderRequest orderData = param.getOrderData();
        KakaoPayApproveResponseDto approveResponseDto = kakaoPayService.approve(approveRequestDto, orderData.toDto());
        KakaoPayApproveResponse responseBody = KakaoPayApproveResponse.of(approveResponseDto);

        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/pay/KAKAO_PAY/cancel")
    public ResponseEntity<KakaoPayCancelResponse> cancelPay(@RequestBody PayRequest<KakaoPayCancelRequest> param) {
        KakaoPayCancelRequest cancelRequest = param.getData();
        KakaoPayCancelRequestDto cancelRequestDto = cancelRequest.toDto();

        KakaoPayCancelResponseDto cancelResponseDto = kakaoPayService.cancel(cancelRequestDto);
        KakaoPayCancelResponse responseBody = KakaoPayCancelResponse.of(cancelResponseDto);

        return ResponseEntity.ok().body(responseBody);
    }
}
