package com.learnjava.parallelstreams;

import java.util.List;

public class ReduceExample {

    // In case of empty list, these functions will still return the identity value.
    // be careful while passing the identity value.
    // In case of sum, if we pass 1 instead of 0 then
    // For parallel stream, since it splits data into chucks of size 1
    // each chuck will have identity as 1 and the expected sum and result will differ by identity*size of input list.
    // For sequential stream, since no split happens so actual and expected result will differ by identity.
    public int reduceSum(List<Integer> inputList) {
        return inputList
                .parallelStream()
                .reduce(0, Integer::sum);
    }

    public int reduceMultiply(List<Integer> inputList) {
        return inputList
                .parallelStream()
                .reduce(1, (x, y) -> x*y);
    }
}
