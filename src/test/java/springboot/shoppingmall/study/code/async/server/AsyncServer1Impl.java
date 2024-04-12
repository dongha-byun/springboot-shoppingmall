package springboot.shoppingmall.study.code.async.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncServer1Impl implements AsyncServer1{

    @Override
    public void logic() {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        log.info("async 1 logic => {}ms", end-start);
    }
}
