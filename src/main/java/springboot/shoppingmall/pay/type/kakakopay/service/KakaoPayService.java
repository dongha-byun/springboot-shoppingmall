package springboot.shoppingmall.pay.type.kakakopay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.order.application.dto.OrderCreateDto;
import springboot.shoppingmall.pay.type.kakakopay.domain.KakaoPayClient;
import springboot.shoppingmall.pay.type.kakakopay.domain.KakaoPayInterfaceFormDataConverter;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyResponseDto;

@RequiredArgsConstructor
@Service
public class KakaoPayService {
    private final KakaoPayClient kakaoPayClient;
    private final KakaoPayInterfaceFormDataConverter formDataConverter;
    private final OrderClient orderClient;

    public KakaoPayReadyResponseDto ready(KakaoPayReadyRequestDto readyRequestDto) {
        MultiValueMap<String, Object> requestFormData = formDataConverter.convertReadyFormData(readyRequestDto);
        return kakaoPayClient.ready(requestFormData);
    }

    public KakaoPayApproveResponseDto approve(KakaoPayApproveRequestDto approveRequestDto, OrderCreateDto orderCreateDto) {
        MultiValueMap<String, Object> requestFormData = formDataConverter.convertApproveFormData(approveRequestDto);
        KakaoPayApproveResponseDto approveResult = kakaoPayClient.approve(requestFormData);

        // 여기서 order end-point 를 콜 해야함 ?
        // 의존이 너무 많이 엮이는거 같지 않냐
        // 그렇다고 카프카 같은 메세지 큐로 하기에도 애매한데
        // 이런건 보통 어케하누
        // orderClient.order();
        // 결제니까 이렇게 하는것도 좋을거 같긴 해.
        // 그럼 순서를 어케하지?
        // 1. 주문 정보를 생성하고, 결제 상태를 READY 로 한다.
        // 2. 생성된 주문정보를 가지고 결제승인을 요청한다.
        // 3. 결제가 승인되면, 주문정보의 결제상태를 COMPLETE 로 변경한다.
        // 4. 1~3을 한 트랜잭션에서 수행한다.
        // 아니면,
        // 1. 결제 승인을 요청한다.
        // 2. 결제 승인이 완료된 후, 주문 정보를 생성한다. (API)
        // 3. 생성한 주문 정보를 return 받는다.
        // 4. return 받은 정보를 응답으로 내보낸다.(?)

        // 결제에서 주문을 호출하기엔 세팅해야할 파라미터가 너무많다.
        // 주문을 처음에 실행하고, 그 정보를 기반으로 결제를 호출해보자.
        // 1. 주문정보 생성
        // 2.
        // 한 트랙잭션으로 가두면, 결제상태는 일단 보류할 수 있다.

        // 아니야
        // 일단 여기서 호출해봐 그냥 직관적으로
        String userId = approveRequestDto.getPartner_user_id();
        orderClient.order(Long.parseLong(userId), orderCreateDto);

        return approveResult;
    }

    public KakaoPayCancelResponseDto cancel(KakaoPayCancelRequestDto cancelRequestDto) {
        MultiValueMap<String, Object> requestFormData = formDataConverter.convertCancelFormData(cancelRequestDto);
        return kakaoPayClient.cancel(requestFormData);
    }
}
