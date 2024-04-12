package springboot.shoppingmall.study.code.async.server;

import org.springframework.scheduling.annotation.Async;

public interface AsyncServer1 {

    @Async
    void logic();
}
