package com.learnjava.helloworldservice;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.LoggerUtil.log;

public class HelloWorldService {

    public String helloWorld() {
        delay(1000);
        log("Inside helloWorld");
        return "Hello World!";
    }

    public String hello() {
        delay(1000);
        log("inside hello");
        return "hello";
    }

    public String world() {
        delay(1000);
        log("inside world");
        return " world!";
    }

    public CompletableFuture<String> worldFuture(String input) {
        return CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return input + " world!";
        });
    }
}
