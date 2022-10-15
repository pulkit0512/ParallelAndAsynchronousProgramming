package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

import static com.learnjava.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayListSpliteratorTest {

    // Not a major performance difference in sequential and parallel approach.
    // Reason is arraylist is an indexed collection, so when parallel() is invoked
    // arraylist spliterator can splice the data into chunks really well.
    // Each and every collection has a different Spliterator Implementation

    private final ArrayListSpliterator arraySpliterator = new ArrayListSpliterator();
    int size = 1000000;

    @BeforeEach
    void setUp() {
        stopWatchReset();
    }

    @RepeatedTest(10)
    void multiplyByValue() {
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        List<Integer> result = arraySpliterator.multiplyByValue(inputList, 2, false);

        assertEquals(size, result.size());
    }

    @RepeatedTest(10)
    void multiplyByValue_parallel() {
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);

        List<Integer> result = arraySpliterator.multiplyByValue(inputList, 2, true);

        assertEquals(size, result.size());
    }
}