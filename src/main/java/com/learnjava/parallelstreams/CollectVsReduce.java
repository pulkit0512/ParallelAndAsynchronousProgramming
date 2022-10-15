package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import java.util.List;
import java.util.stream.Collectors;

import static com.learnjava.util.LoggerUtil.log;

public class CollectVsReduce {

    // In this case better to use collect, because reduce produces immutable objects. And in this case perform string concatenation.
    // Whereas collect produces mutable objects using StringBuilder.append and at last return output.
    private static String collect() {
        List<String> names = DataSet.namesList();

        return names
                .parallelStream()
                .collect(Collectors.joining());
    }

    private static String reduce() {
        List<String> names = DataSet.namesList();

        return names
                .parallelStream()
                .reduce("", (s1,s2) -> s1+s2);
    }

    public static void main(String[] args) {
        log("Output using Collect: " + collect());

        log("Output using Reduce: " + reduce());
    }
}
