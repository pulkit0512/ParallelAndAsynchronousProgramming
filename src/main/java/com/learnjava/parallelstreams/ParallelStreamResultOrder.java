package com.learnjava.parallelstreams;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.learnjava.util.LoggerUtil.log;

public class ParallelStreamResultOrder {

    public static List<Integer> listOrder(List<Integer> inputList) {

        return inputList.parallelStream()
                .map(integer -> integer*2)
                .collect(Collectors.toList());
    }

    public static Set<Integer> setOrder(List<Integer> inputList){

        return inputList.parallelStream()
                .map(integer -> integer*2)
                .collect(Collectors.toSet());
    }

    public static void main(String[] args) {
        List<Integer> inputList = List.of(1,2,3,4,5,6,7,8,9,10,11,12);
        log("input list: " + inputList);

        log("result list is: " + listOrder(inputList));

        // will not be in order since hashSet is inordered data structure.
        log("result set is: " + setOrder(inputList));
    }
}
