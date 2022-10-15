package com.learnjava.parallelstreams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;

public class ArrayListSpliterator {

    public List<Integer> multiplyByValue(ArrayList<Integer> inputList, int multiple, boolean isParallel) {
        startTimer();

        Stream<Integer> integerStream = inputList.stream();

        if(isParallel) {
            integerStream = integerStream.parallel();
        }

        List<Integer> result = integerStream
                .map(integer -> integer*multiple)
                .collect(Collectors.toList());

        timeTaken();

        return result;
    }
}
