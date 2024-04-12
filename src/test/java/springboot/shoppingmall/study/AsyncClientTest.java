package springboot.shoppingmall.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.shoppingmall.study.code.async.AsyncClient;

@SpringBootTest
public class AsyncClientTest {

    @Autowired
    AsyncClient asyncClient;

    @Test
    @DisplayName("@Async를 적용해봅니다.")
    void async() {
        asyncClient.execute();
    }
}
