package springboot.shoppingmall.pay.type.kakakopay.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyRequestDto;
import springboot.shoppingmall.pay.type.kakakopay.web.approve.KakaoPayApproveRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.ready.KakaoPayReadyRequest;

@Component
public class KakaoPayInterfaceFormDataConverter {

    @Value("pay.api.kakao.cid")
    private String cid;

    public MultiValueMap<String, Object> convertReadyFormData (KakaoPayReadyRequestDto readyRequestDto) {
        MultiValueMap<String, Object> map = readyRequestDto.toFormData();
        map.add("cid", cid);

        return map;
    }

    public MultiValueMap<String, Object> convertApproveFormData (KakaoPayApproveRequestDto approveRequest) {
        MultiValueMap<String, Object> map = approveRequest.toFormData();
        map.add("cid", cid);

        return map;
    }

    public MultiValueMap<String, Object> convertCancelFormData (KakaoPayCancelRequestDto cancelRequest) {
        MultiValueMap<String, Object> map = cancelRequest.toFormData();
        map.add("cid", cid);

        return map;
    }
}
