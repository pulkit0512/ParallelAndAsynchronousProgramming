package com.learnjava.forkjoin;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class StringTransformExample {

    public static void main(String[] args) {
        stopWatch.start();
        List<String> names = DataSet.namesList();

        List<String> convertedNames = names.parallelStream() // 1) Fork
                .map(String::toUpperCase) // 2) Process Sequentially to upper case
                .collect(Collectors.toList()); // 3) Join

        stopWatch.stop();

        log("Uppercase names: " + convertedNames);
        log("Time Taken is: " + stopWatch.getTime());
    }
}
