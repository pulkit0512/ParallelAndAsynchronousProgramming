package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class StringTransformExample {

    public static void main(String[] args) {

        List<String> names = DataSet.namesList();
        transformToUpperCase(names);

        transformToAddLengthToStringStream(names);

        transformToAddLengthToStringParallelStream(names);
    }

    private static void transformToUpperCase(List<String> names) {
        stopWatch.start();

        List<String> convertedNames = names.parallelStream() // 1) Fork
                .map(String::toUpperCase) // 2) Process Sequentially to upper case
                .collect(Collectors.toList()); // 3) Join

        stopWatch.stop();

        log("Uppercase names: " + convertedNames);
        log("Time Taken is: " + stopWatch.getTime());
    }

    private static void transformToAddLengthToStringStream(List<String> names) {
        stopWatch.reset();

        stopWatch.start();

        List<String> convertedNames = names.stream()
                .map(StringTransformExample::addLengthToString)
                .collect(Collectors.toList());

        stopWatch.stop();

        log("Converted names: " + convertedNames);
        log("Time Taken using stream is: " + stopWatch.getTime());
    }

    private static void transformToAddLengthToStringParallelStream(List<String> names) {
        stopWatch.reset();

        stopWatch.start();

        // No change in code just invoke parallelStream() instead of stream()
        List<String> convertedNames = names.parallelStream() // 1) Fork
                .map(StringTransformExample::addLengthToString) // 2) Process Sequentially to add length to string.
                .collect(Collectors.toList()); // 3) Join

        stopWatch.stop();

        log("Converted names: " + convertedNames);
        log("Time Taken using parallelStream is: " + stopWatch.getTime());
    }

    private static String addLengthToString(String name) {
        delay(500);
        return name.length() +"-" + name;
    }
}
