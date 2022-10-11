package com.learnjava.util;

import org.apache.commons.lang3.time.StopWatch;

import static com.learnjava.util.LoggerUtil.log;
import static java.lang.Thread.sleep;

public class CommonUtil {

    private CommonUtil() {}

    public static final StopWatch stopWatch = new StopWatch();

    public static void delay(long milliseconds) {
        try {
            sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LoggerUtil.log("Exception is :" + e.getMessage());
        }
    }

    public static void startTimer(){
        stopWatch.start();
    }

    public static void timeTaken(){
        stopWatch.stop();
        log("Total Time Taken : " +stopWatch.getTime());
    }

    public static void stopWatchReset(){
        stopWatch.reset();
    }

    public static  int noOfCores(){
        return Runtime.getRuntime().availableProcessors();
    }
}
