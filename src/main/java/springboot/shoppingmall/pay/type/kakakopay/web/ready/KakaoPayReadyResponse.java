package springboot.shoppingmall.pay.type.kakakopay.web.ready;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyResponseDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoPayReadyResponse {
    private String tid;
    private String next_redirect_app_url;
    private String next_redirect_mobile_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String ios_app_scheme;
    private LocalDateTime created_at;

    public static KakaoPayReadyResponse of(KakaoPayReadyResponseDto dto) {
        return new KakaoPayReadyResponse(
                dto.getTid(),
                dto.getNext_redirect_app_url(),
                dto.getNext_redirect_mobile_url(),
                dto.getNext_redirect_pc_url(),
                dto.getAndroid_app_scheme(),
                dto.getIos_app_scheme(),
                dto.getCreated_at()
        );
    }
}
