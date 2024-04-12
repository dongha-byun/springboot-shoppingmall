package springboot.shoppingmall.study.code.async.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncServer2Impl implements AsyncServer2{

    @Override
    public void logic() {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        log.info("async 2 logic => {}ms", end-start);
    }
}
