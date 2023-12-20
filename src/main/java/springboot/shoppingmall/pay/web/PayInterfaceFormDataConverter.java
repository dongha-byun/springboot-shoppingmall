package springboot.shoppingmall.pay.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayApproveRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayReadyRequest;

@Component
public class PayInterfaceFormDataConverter {

    @Value("pay.api.kakao.cid")
    private String cid;

    public MultiValueMap<String, Object> convertReadyFormData (KakaoPayReadyRequest readyRequest) {
        MultiValueMap<String, Object> map = readyRequest.toFormData();
        map.add("cid", cid);
        return map;
    }

    public MultiValueMap<String, Object> convertApproveFormData (KakaoPayApproveRequest approveRequest) {
        MultiValueMap<String, Object> map = approveRequest.toFormData();
        map.add("cid", cid);

        return map;
    }

    public MultiValueMap<String, Object> convertCancelFormData (KakaoPayCancelRequest cancelRequest) {
        MultiValueMap<String, Object> map = cancelRequest.toFormData();
        map.add("cid", cid);

        return map;
    }
}
