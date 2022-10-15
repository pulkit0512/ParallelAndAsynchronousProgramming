package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.learnjava.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ParallelStreamPoorPerformanceTest {
    // In all below cases Sequential performs better than Parallel.
    // Since a lot of boxing and unboxing is involved.

    ParallelStreamPoorPerformance streamPoorPerformance = new ParallelStreamPoorPerformance();
    int size = 1000000;

    @BeforeEach
    void setUp() {
        stopWatchReset();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void sumUsingLongStream(boolean isParallel) {
        long sum = streamPoorPerformance.sumUsingLongStream(size, isParallel);

        assertEquals(500000500000L, sum);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void sumUsingList(boolean isParallel) {
        List<Integer> inputList = DataSet.generateArrayList(size);
        long sum = streamPoorPerformance.sumUsingList(inputList, isParallel);

        assertEquals(500000500000L, sum);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void sumUsingIterate(boolean isParallel) {
        long sum = streamPoorPerformance.sumUsingIterate(size, isParallel);

        assertEquals(500000500000L, sum);
    }
}