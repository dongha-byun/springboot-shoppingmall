package springboot.shoppingmall.study.code.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.study.code.async.server.AsyncServer1;
import springboot.shoppingmall.study.code.async.server.AsyncServer2;

@Slf4j
@Component
public class AsyncClient {

    private final AsyncServer1 asyncServer1;
    private final AsyncServer2 asyncServer2;

    public AsyncClient(AsyncServer1 asyncServer1, AsyncServer2 asyncServer2) {
        this.asyncServer1 = asyncServer1;
        this.asyncServer2 = asyncServer2;
    }

    public void execute() {
        log.info("execute method start");
        long start = System.currentTimeMillis();
        asyncServer1.logic();
        asyncServer2.logic();
        long end = System.currentTimeMillis();
        log.info("execute method end");
        log.info("total time = {}ms", end-start);
    }
}

