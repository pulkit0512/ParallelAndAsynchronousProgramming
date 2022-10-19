package com.learnjava.completablefuture;

import com.learnjava.helloworldservice.HelloWorldService;
import com.learnjava.util.CommonUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private final HelloWorldService helloWorldService;

    private static final String HI_COMPLETABLE_FUTURE = " Hi Completable Future";

    public CompletableFutureHelloWorld(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    public CompletableFuture<String> hello() {
        return CompletableFuture.supplyAsync(helloWorldService::hello)
                .thenApply(String::toUpperCase);
    }

    public CompletableFuture<String> helloWorldWithSize() {
        return CompletableFuture.supplyAsync(helloWorldService::helloWorld)
                .thenApply(String::toUpperCase)
                .thenApply(s -> s.length() +" - " + s);
    }

    public CompletableFuture<String> helloWorldThenCompose() {
        // We can use compose in case of dependent tasks.
        // here, worldFuture function is dependent on output of hello function.
        // here, it waits for the hello function to complete before starting its execution.
        return CompletableFuture.supplyAsync(helloWorldService::hello)
                .thenCompose(helloWorldService::worldFuture)
                .thenApply(String::toUpperCase);
    }

    public String helloWorldImperativeApproach() {
        startTimer();
        String hello = helloWorldService.hello();
        String world = helloWorldService.world();

        String res = (hello+world).toUpperCase();
        timeTaken();

        return res;
    }

    public String helloWorldTwoAsyncCalls() {
        startTimer();
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);

        String res = helloFuture
                .thenCombine(worldFuture, (hello, world) -> hello + world)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return res;
    }

    public String helloWorldThreeAsyncCalls() {
        startTimer();
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return HI_COMPLETABLE_FUTURE;
        });

        String res = helloFuture
                .thenCombine(worldFuture, (hello, world) -> hello + world)
                .thenCombine(hiFuture, (prev, curr) -> prev+curr)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return res;
    }

    public String helloWorldThreeAsyncCallsLog() {
        startTimer();
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(300);
            return HI_COMPLETABLE_FUTURE;
        });

        String res = helloFuture
                .thenCombine(worldFuture, (hello, world) -> {
                    log("thenCombine: Hello/World");
                    return hello + world;
                })
                .thenCombine(hiFuture, (prev, curr) -> {
                    log("thenCombine: prev/curr");
                    return prev+curr;
                })
                .thenApply(s -> {
                    log("thenApply Uppercase transformation");
                    return s.toUpperCase();
                })
                .join();
        timeTaken();

        return res;
    }

    public String helloWorldThreeAsyncCallsLogAsync() {
        startTimer();
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(400);
            return HI_COMPLETABLE_FUTURE;
        });

        String res = helloFuture
                .thenCombineAsync(worldFuture, (hello, world) -> {
                    log("thenCombineAsync: Hello/World");
                    return hello + world;
                })
                .thenCombineAsync(hiFuture, (prev, curr) -> {
                    log("thenCombineAsync: prev/curr");
                    return prev+curr;
                })
                .thenApplyAsync(s -> {
                    log("thenApplyAsync Uppercase transformation");
                    return s.toUpperCase();
                })
                .join();
        timeTaken();

        return res;
    }

    public String helloWorldThreeAsyncCallsCustomThreadPool() {
        startTimer();
        // Creates a Custom Thread Pool of size equal to number of Cores.
        ExecutorService executorService = Executors.newFixedThreadPool(CommonUtil.noOfCores());
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello, executorService);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world, executorService);
        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(300);
            return HI_COMPLETABLE_FUTURE;
        }, executorService);

        String res = helloFuture
                .thenCombine(worldFuture, (hello, world) -> {
                    log("thenCombine: Hello & World");
                    return hello + world;
                })
                .thenCombine(hiFuture, (prev, curr) -> {
                    log("thenCombine: prev & curr");
                    return prev+curr;
                })
                .thenApply(s -> {
                    log("thenApply Uppercase transformation");
                    return s.toUpperCase();
                })
                .join();
        timeTaken();

        return res;
    }

    public String helloWorldThreeAsyncCallsCustomThreadPoolAsync() {
        startTimer();
        // Creates a Custom Thread Pool of size equal to number of Cores.
        ExecutorService executorService = Executors.newFixedThreadPool(CommonUtil.noOfCores());
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello, executorService);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world, executorService);
        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(400);
            return HI_COMPLETABLE_FUTURE;
        }, executorService);

        String res = helloFuture
                .thenCombineAsync(worldFuture, (hello, world) -> {
                    log("thenCombineAsync: Hello & World");
                    return hello + world;
                }, executorService)
                .thenCombineAsync(hiFuture, (prev, curr) -> {
                    log("thenCombineAsync: prev & curr");
                    return prev+curr;
                }, executorService)
                .thenApplyAsync(s -> {
                    log("thenApplyAsync Uppercase transformation");
                    return s.toUpperCase();
                }, executorService)
                .join();
        timeTaken();

        return res;
    }

    public String helloWorldFourAsyncCalls() {
        startTimer();
        CompletableFuture<String> hiFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return HI_COMPLETABLE_FUTURE;
        });
        CompletableFuture<String> helloFuture = CompletableFuture.supplyAsync(helloWorldService::hello);
        CompletableFuture<String> worldFuture = CompletableFuture.supplyAsync(helloWorldService::world);
        CompletableFuture<String> byeFuture = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return " Bye!";
        });

        String res = helloFuture
                .thenCombine(worldFuture, (hello, world) -> hello + world)
                .thenCombine(hiFuture, (prev, curr) -> prev+curr)
                .thenCombine(byeFuture, (prev, curr) -> prev+curr)
                .thenApply(String::toUpperCase)
                .join();
        timeTaken();

        return res;
    }

    public static void main(String[] args) {
        HelloWorldService hws = new HelloWorldService();

        // supplyAsync works on responsive behavior of reactive programming.
        // It returns immediately after initiating a background task.
        // thenAccept works on message driven behaviour of reactive programming.
        // the message/event is passed to thenAccept from supplyAsync,
        // and then it performs the required task on the event.
        CompletableFuture.supplyAsync(hws::helloWorld) // runs the complete work in common fork join pool
                .thenApply(String::toUpperCase) // Transform the data from one form to another.
                .thenAccept(res -> log("Result is: " + res))
                .join(); // This will block main thread, use this if we want result in order.

        // Without any further delay, since CompletableFuture.supplyAsync is responsive in nature.
        // It initiates a background task.
        // It returns immediately, and the main thread is free now.
        // To log the result is handled by another thread from the CommonForkJoinPool
        // Since the main thread is free and execution completes before the delay of 1 second in hello world service
        // exhausts. That's why in output we only get "Done!" logged.
        log("Done!");

        // Now if we add delay of 2seconds, then main thread will be blocked for 2 seconds and by that time
        // delay of hello world service will exhaust, and we will get the result from the service.
        // We have another method called "join" which will block main thread until we get the result from the hello world
        // service. So in case you want to get result in order then we can use join.
        delay(2000);
    }
}
