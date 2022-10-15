package com.learnjava.parallelstreams;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReduceExampleTest {

    ReduceExample reduceExample = new ReduceExample();

    @Test
    void reduceSum() {
        List<Integer> inputList = List.of(1, 2, 3, 4, 5, 6, 7, 8);

        int sum = reduceExample.reduceSum(inputList);

        assertEquals(36, sum);
    }

    @Test
    void reduceSumEmptyList() {
        List<Integer> inputList = new ArrayList<>();

        int sum = reduceExample.reduceSum(inputList);

        assertEquals(0, sum);
    }

    @Test
    void reduceMultiply() {
        List<Integer> inputList = List.of(1, 2, 3, 4, 5);

        int multiply = reduceExample.reduceMultiply(inputList);

        assertEquals(120, multiply);
    }

    @Test
    void reduceMultiplyEmptyList() {
        List<Integer> inputList = new ArrayList<>();

        int multiply = reduceExample.reduceMultiply(inputList);

        assertEquals(1, multiply);
    }
}