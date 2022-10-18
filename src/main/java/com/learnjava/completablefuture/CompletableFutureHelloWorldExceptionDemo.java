package com.learnjava.completablefuture;

import com.learnjava.helloworldservice.HelloWorldService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorldExceptionDemo {

    private final HelloWorldService helloWorldService;
    private static final String HI_COMPLETABLE_FUTURE = " Hi Completable Future";

    public CompletableFutureHelloWorldExceptionDemo(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    public String helloWorldThreeAsyncCallsHandle() {
        startTimer();
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);

        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return HI_COMPLETABLE_FUTURE;
        });

        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);

        // We can have multiple handle calls in the completable future pipeline.
        // And where handle is placed in the pipeline will have a significant effect on overall core behavior.
        String res = helloFuture
                // This will handle any exception that will occur in hello call.
                // Will catch the exception and then provide a recoverable value.
                // handle function will be called in both cases (success and failure case)
                // so we need to code for both cases
                .handle((result, ex) -> {
                    log("Handle: Result is: " + result);
                    if(ex!=null) {
                        log("Handle: Exception is: " + ex.getMessage());
                        return "";
                    }else{
                        return result;
                    }
                }) // ""
                .thenCombine(worldFuture, (hello, world) -> hello + world)
                .handle((result, ex) -> {
                    log("Handle: Result is: " + result);
                    if(ex!=null) {
                        log("Handle: Exception after world call is: " + ex.getMessage());
                        return "";
                    }else{
                        return result;
                    }
                }) // ""
                .thenCombine(hiFuture, (prev, curr) -> prev+curr)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return res;
    }

    public String helloWorldThreeAsyncCallsExceptionally() {
        startTimer();
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(500);
            return HI_COMPLETABLE_FUTURE;
        });

        // We can have multiple exceptionally calls in the completable future pipeline.
        // And where exceptionally is placed in the pipeline will have a significant effect on overall core behavior.
        String res = helloFuture
                // This will handle any exception that will occur in hello call.
                // Will catch the exception and then provide a recoverable value.
                // exceptionally function will be called in failure cases only
                // no need to code for the happy path inside exceptionally call.
                .exceptionally(ex -> {
                    log("Exceptionally: Exception is: " + ex.getMessage());
                    return "";
                }) // ""
                .thenCombine(worldFuture, (hello, world) -> hello + world)
                .exceptionally(ex -> {
                    log("Exceptionally: Exception after world call is: " + ex.getMessage());
                    return "";
                }) // ""
                .thenCombine(hiFuture, (prev, curr) -> prev+curr)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return res;
    }

    public String helloWorldThreeAsyncCallsWhenComplete() {
        startTimer();
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);

        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(500);
            return HI_COMPLETABLE_FUTURE;
        });

        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);

        // We can have multiple whenComplete calls in the completable future pipeline.
        String res = helloFuture
                // This will handle any exception that will occur in hello call.
                // Will catch the exception, but it cannot provide a recoverable value.
                // whenComplete function will be called in both cases (success and failure case)
                // so we need to code for both cases
                // Once the exception occurs we can't recover from it. And the exception propagates.
                .whenComplete((result, ex) -> {
                    log("WhenComplete: Result is: " + result);
                    if(ex!=null) {
                        log("WhenComplete: Exception is: " + ex.getMessage());
                    }
                }) // ""
                .thenCombine(worldFuture, (hello, world) -> hello + world)
                .whenComplete((result, ex) -> {
                    log("WhenComplete: Result is: " + result);
                    if(ex!=null) {
                        log("WhenComplete: Exception after world call is: " + ex.getMessage());
                    }
                }) // ""
                .exceptionally(ex -> {
                    log("WhenComplete: Exception after thenCombine is: " + ex.getMessage());
                    return "";
                }) // ""
                .thenCombine(hiFuture, (prev, curr) -> prev+curr)
                .thenApply(String::toUpperCase)
                .join();

        timeTaken();

        return res;
    }
}
