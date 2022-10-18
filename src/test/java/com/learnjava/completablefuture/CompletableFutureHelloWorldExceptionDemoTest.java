package com.learnjava.completablefuture;

import com.learnjava.helloworldservice.HelloWorldService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.learnjava.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionDemoTest {

    @Mock
    private HelloWorldService helloWorldService = mock(HelloWorldService.class);

    @InjectMocks
    private CompletableFutureHelloWorldExceptionDemo helloWorldException;

    @BeforeEach
    void setUp() {
        stopWatchReset();
    }

    @Test
    void helloWorldThreeAsyncCalls() {
        Mockito.when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        Mockito.when(helloWorldService.world()).thenCallRealMethod();

        String res = helloWorldException.helloWorldThreeAsyncCallsHandle();

        assertEquals(" WORLD! HI COMPLETABLE FUTURE", res);
    }

    @Test
    void helloWorldThreeAsyncCalls2() {
        Mockito.when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        Mockito.when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred!"));

        String res = helloWorldException.helloWorldThreeAsyncCallsHandle();

        assertEquals(" HI COMPLETABLE FUTURE", res);
    }

    @Test
    void helloWorldThreeAsyncCalls3() {
        Mockito.when(helloWorldService.hello()).thenCallRealMethod();
        Mockito.when(helloWorldService.world()).thenCallRealMethod();

        String res = helloWorldException.helloWorldThreeAsyncCallsHandle();

        assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", res);
    }

    @Test
    void helloWorldThreeAsyncCallsExceptionally() {
        Mockito.when(helloWorldService.hello()).thenCallRealMethod();
        Mockito.when(helloWorldService.world()).thenCallRealMethod();

        String res = helloWorldException.helloWorldThreeAsyncCallsExceptionally();

        assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", res);
    }

    @Test
    void helloWorldThreeAsyncCallsExceptionally2() {
        Mockito.when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        Mockito.when(helloWorldService.world()).thenThrow(new RuntimeException("Exception Occurred!"));

        String res = helloWorldException.helloWorldThreeAsyncCallsExceptionally();

        assertEquals(" HI COMPLETABLE FUTURE", res);
    }

    @Test
    void helloWorldThreeAsyncCallsWhenComplete() {
        Mockito.when(helloWorldService.hello()).thenCallRealMethod();
        Mockito.when(helloWorldService.world()).thenCallRealMethod();

        String res = helloWorldException.helloWorldThreeAsyncCallsWhenComplete();

        assertEquals("HELLO WORLD! HI COMPLETABLE FUTURE", res);
    }

    @Test
    void helloWorldThreeAsyncCallsWhenComplete2() {
        Mockito.when(helloWorldService.hello()).thenThrow(new RuntimeException("Exception Occurred!"));
        // Exception will propagate to this call as well, and we won't get actual value from this call.
        // We will reach to whenComplete error block for this call as well.
        Mockito.when(helloWorldService.world()).thenCallRealMethod();

        String res = helloWorldException.helloWorldThreeAsyncCallsWhenComplete();

        assertEquals(" HI COMPLETABLE FUTURE", res);
    }
}