package springboot.shoppingmall.api.address;

import java.net.URL;
import org.springframework.stereotype.Component;

@Component
public class KaKaoAddressSearchService implements AddressSearchService {

    private static final String KAKAO_AUTHORIZATION_HEADER = "KakaoAK";
    private static final String URL = "https://dapi.kakao.com/v2/local/search/address.json?analyze_type=similar";

    private static final String REST_API_KEY = "7297eeb263e176a5b1b02c2b507ca1a7";

    @Override
    public void searchAddress(String searchText) {
        String reqUrl = URL + "&query=" + URLEncodingUtil.encoding(searchText, "UTF-*");

    }
}
