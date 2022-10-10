package com.learnjava.forkjoin;

import com.learnjava.util.DataSet;

import java.util.ArrayList;
import java.util.List;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class StringTransformSequentialExample {

    public static void main(String[] args) {
        stopWatch.start();
        List<String> names = DataSet.namesList();
        List<String> convertedNames = new ArrayList<>();

        names.forEach(name -> convertedNames.add(addNameLengthTransform(name)));

        stopWatch.stop();

        log("Transformed output: " + convertedNames);
        log("Time Taken is: " + stopWatch.getTime());
    }

    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length() + "-" + name;
    }
}
