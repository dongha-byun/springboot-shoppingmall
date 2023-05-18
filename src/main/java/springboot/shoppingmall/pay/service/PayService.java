package springboot.shoppingmall.pay.service;

import org.springframework.util.MultiValueMap;

public interface PayService {

    Object ready(MultiValueMap<String, String> formData);

    Object approve(MultiValueMap<String, String> formData);

    Object cancel(MultiValueMap<String, Object> formData);
}
