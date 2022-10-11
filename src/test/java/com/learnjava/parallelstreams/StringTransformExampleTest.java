package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringTransformExampleTest {
    StringTransformExample stringTransformExample = new StringTransformExample();

    @Test
    void transformToAddLengthToStringParallelStream() {
        List<String> inputList = DataSet.namesList();

        List<String> resultList = stringTransformExample.transformToAddLengthToStringParallelStream(inputList);

        assertEquals(inputList.size(), resultList.size());

        resultList.forEach(name -> assertTrue(name.contains("-")));
    }
}