package springboot.shoppingmall.pay.service;

import org.springframework.util.MultiValueMap;

public interface PayService {

    Object ready(MultiValueMap<String, Object> formData);

    Object approve(MultiValueMap<String, Object> formData);

    Object cancel(MultiValueMap<String, Object> formData);
}
