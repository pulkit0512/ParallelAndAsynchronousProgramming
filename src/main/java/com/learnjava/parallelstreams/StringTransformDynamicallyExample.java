package com.learnjava.parallelstreams;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.delay;

public class StringTransformDynamicallyExample {

    public List<String> transformToAddLengthToString(List<String> names) {

        return names
                .parallelStream()
                .map(StringTransformDynamicallyExample::addLengthToString)
                .collect(Collectors.toList());
    }

    public List<String> transformToAddLengthToStringDynamically(List<String> names, boolean isParallel) {

        Stream<String> namesStream = names.stream();

        if(isParallel){
            namesStream = namesStream.parallel();
        }


        return namesStream
                .map(StringTransformDynamicallyExample::addLengthToString)
                .collect(Collectors.toList());
    }

    public List<String> stringToLowerCase(List<String> names) {

        return names.parallelStream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private static String addLengthToString(String name) {
        delay(500);
        return name.length() +"-" + name;
    }
}
