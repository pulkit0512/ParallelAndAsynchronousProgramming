package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class StringTransformDynamicallyExampleTest {

    StringTransformDynamicallyExample example = new StringTransformDynamicallyExample();
    private final List<String> inputList = DataSet.namesList();

    @BeforeEach
    void setUp() {
        stopWatchReset();
    }

    @Test
    void transformToAddLengthToString() {
        startTimer();
        List<String> resultList = example.transformToAddLengthToString(inputList);
        timeTaken();

        assertEquals(inputList.size(), resultList.size());

        resultList.forEach(name -> assertTrue(name.contains("-")));
    }

    @ParameterizedTest // Takes the values from ValueSource and pass it to the test,
    // each value will be considered as an individual test
    @ValueSource(booleans = {false, true})
    void transformToAddLengthToStringDynamically(boolean isParallel) {
        startTimer();
        List<String> resultList = example.transformToAddLengthToStringDynamically(inputList, isParallel);
        timeTaken();

        assertEquals(inputList.size(), resultList.size());

        resultList.forEach(name -> assertTrue(name.contains("-")));
    }

    @Test
    void stringToLowerCase() {
        startTimer();
        List<String> resultList = example.stringToLowerCase(inputList);
        timeTaken();

        assertEquals(inputList.size(), resultList.size());
    }
}