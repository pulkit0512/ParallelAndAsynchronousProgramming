package com.learnjava.util;

public class LoggerUtil {

    private LoggerUtil() {

    }

    public static void log(String message){

        System.out.println("[" + Thread.currentThread().getName() +"] - " + message);

    }
}
