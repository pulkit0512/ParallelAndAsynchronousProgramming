package com.learnjava.parallelstreams;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;

public class LinkedListSpliterator {

    public List<Integer> multiplyByValue(LinkedList<Integer> inputList, int multiple, boolean isParallel) {
        startTimer();

        Stream<Integer> integerStream = inputList.parallelStream();

        if(!isParallel) {
            integerStream = integerStream.sequential();
        }

        List<Integer> result = integerStream
                .map(integer -> integer*multiple)
                .collect(Collectors.toList());

        timeTaken();

        return result;
    }
}
