package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.LinkedList;
import java.util.List;

import static com.learnjava.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkedListSpliteratorTest {
    // Sequential performs really well as compared to parallel.
    // This is because it is very difficult to split LinkedList into chunks, since it is not an indexed collection.

    LinkedListSpliterator linkedListSpliterator = new LinkedListSpliterator();
    int size = 1000000;

    @BeforeEach
    void setUp() {
        stopWatchReset();
    }

    @RepeatedTest(10)
    void multiplyByValue() {
        LinkedList<Integer> inputList = DataSet.generateLinkedList(size);

        List<Integer> result = linkedListSpliterator.multiplyByValue(inputList, 2, false);

        assertEquals(size, result.size());
    }

    @RepeatedTest(10)
    void multiplyByValue_parallel() {
        LinkedList<Integer> inputList = DataSet.generateLinkedList(size);

        List<Integer> result = linkedListSpliterator.multiplyByValue(inputList, 2, true);

        assertEquals(size, result.size());
    }
}