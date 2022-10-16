package com.learnjava.completablefuture;

import com.learnjava.helloworldservice.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {
    HelloWorldService helloWorldService = new HelloWorldService();
    CompletableFutureHelloWorld completableFutureHelloWorld = new CompletableFutureHelloWorld(helloWorldService);

    @Test
    void hello() {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.hello();

        completableFuture
                .thenAccept(s -> assertEquals("HELLO", s))
                // If we don't add join here then completable future will release test worker thread
                // And actual test won't be performed, since we have a delay of 1 sec.
                // So to get the actual test performed we need to block test worker thread.
                .join();
    }

    @Test
    void helloWorldThenCompose() {
        stopWatchReset();
        startTimer();

        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorldThenCompose();

        completableFuture
                .thenAccept(s -> assertEquals("HELLO WORLD!", s))
                .join();

        timeTaken();
    }

    @Test
    void helloWorldWithSize() {
        CompletableFuture<String> completableFuture = completableFutureHelloWorld.helloWorldWithSize();

        completableFuture
                .thenAccept(s -> assertEquals("12 - HELLO WORLD!", s))
                .join();
    }

    @Test
    void helloWorldTest() {
        String res = completableFutureHelloWorld.helloWorldImperativeApproach();
        assertEquals("HELLO WORLD!", res);

        stopWatchReset();
        log("===== EXECUTE TWO ASYNC CALLS ===== \n");

        String res1 = completableFutureHelloWorld.helloWorldTwoAsyncCalls();
        assertEquals("HELLO WORLD!", res1);

        stopWatchReset();
        log("===== EXECUTE THREE ASYNC CALLS ===== \n");

        String res2 = completableFutureHelloWorld.helloWorldThreeAsyncCalls();
        assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", res2);

        stopWatchReset();
        log("===== EXECUTE FOUR ASYNC CALLS ===== \n");

        String res3 = completableFutureHelloWorld.helloWorldFourAsyncCalls();
        assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE BYE!", res3);
    }
}