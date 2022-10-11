package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.LoggerUtil.log;

public class StringTransformExample {

    public static void main(String[] args) {

        StringTransformExample stringTransformExample = new StringTransformExample();
        List<String> names = DataSet.namesList();
        stringTransformExample.transformToUpperCase(names);
        log("\n");

        stringTransformExample.transformToAddLengthToStringStream(names);
        log("\n");

        List<String> resultList = stringTransformExample.transformToAddLengthToStringParallelStream(names);
        log("Result list: " + resultList);
        log("\n");

        List<String> resultListSequential = stringTransformExample.transformToAddLengthToStringParallelStreamConvertedSequential(names);
        log("Result list sequential: " + resultListSequential);
    }

    public void transformToUpperCase(List<String> names) {
        stopWatch.start();

        List<String> convertedNames = names.parallelStream() // 1) Fork
                .map(String::toUpperCase) // 2) Process Sequentially to upper case
                .collect(Collectors.toList()); // 3) Join

        stopWatch.stop();

        log("Uppercase names: " + convertedNames);
        log("Time Taken is: " + stopWatch.getTime());
    }

    public void transformToAddLengthToStringStream(List<String> names) {
        stopWatchReset();

        stopWatch.start();

        List<String> convertedNames = names.stream()
                .map(StringTransformExample::addLengthToString)
                .collect(Collectors.toList());

        stopWatch.stop();

        log("Converted names: " + convertedNames);
        log("Time Taken using stream is: " + stopWatch.getTime());
    }

    public List<String> transformToAddLengthToStringParallelStream(List<String> names) {
        stopWatchReset();

        stopWatch.start();

        // No change in code just invoke parallelStream() instead of stream()
        List<String> convertedNames = names.parallelStream() // 1) Fork
                .map(StringTransformExample::addLengthToString) // 2) Process Sequentially to add length to string.
                .collect(Collectors.toList()); // 3) Join

        stopWatch.stop();
        log("Time Taken using parallelStream is: " + stopWatch.getTime());

        return convertedNames;
    }

    public List<String> transformToAddLengthToStringParallelStreamConvertedSequential(List<String> names) {
        stopWatchReset();

        stopWatch.start();

        List<String> convertedNames = names
                .parallelStream()
                .map(StringTransformExample::addLengthToString)
                .sequential() // this will change the parallel stream behaviour to sequential
                .parallel() // the last call in the pipeline will be considered, all other calls will be overridden.
                .sequential()
                .collect(Collectors.toList());

        stopWatch.stop();
        log("Time Taken using parallelStream converted to Sequential is: " + stopWatch.getTime());

        return convertedNames;
    }

    private static String addLengthToString(String name) {
        delay(500);
        return name.length() +"-" + name;
    }
}
