package com.learnjava.parallelstreams;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;

public class ParallelStreamPoorPerformance {

    public long sumUsingLongStream(int count, boolean isParallel) {
        startTimer();
        LongStream longStream = LongStream.rangeClosed(0, count);

        if(isParallel)
            longStream = longStream.parallel();

        long sum = longStream.sum();
        timeTaken();

        return sum;
    }

    public long sumUsingList(List<Integer> integerList, boolean isParallel) {
        startTimer();
        Stream<Integer> integerStream = integerList.stream();

        if(isParallel)
            integerStream = integerStream.parallel();

        long sum = integerStream
                .map(i -> (long) i)
                .mapToLong(Long::longValue)
                .sum();

        timeTaken();

        return sum;
    }

    public long sumUsingIterate(int n, boolean isParallel) {
        startTimer();
        Stream<Long> longStream = Stream.iterate(0L, i-> i+1);

        if(isParallel)
            longStream = longStream.parallel();

        long sum = longStream
                .limit(n+1L)
                .reduce(0L, Long::sum);

        timeTaken();

        return sum;
    }
}
