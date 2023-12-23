package springboot.shoppingmall.pay.type.kakakopay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
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

    public KakaoPayReadyResponseDto ready(KakaoPayReadyRequestDto readyRequestDto) {
        MultiValueMap<String, Object> requestFormData = formDataConverter.convertReadyFormData(readyRequestDto);
        return kakaoPayClient.ready(requestFormData);
    }

    public KakaoPayApproveResponseDto approve(KakaoPayApproveRequestDto approveRequestDto) {
        MultiValueMap<String, Object> requestFormData = formDataConverter.convertApproveFormData(approveRequestDto);
        return kakaoPayClient.approve(requestFormData);
    }

    public KakaoPayCancelResponseDto cancel(KakaoPayCancelRequestDto cancelRequestDto) {
        MultiValueMap<String, Object> requestFormData = formDataConverter.convertCancelFormData(cancelRequestDto);
        return kakaoPayClient.cancel(requestFormData);
    }
}
