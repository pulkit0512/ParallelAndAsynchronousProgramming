package com.learnjava.util;

import org.apache.commons.lang3.time.StopWatch;

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
}
