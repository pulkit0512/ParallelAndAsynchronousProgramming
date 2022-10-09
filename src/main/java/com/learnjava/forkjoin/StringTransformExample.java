package com.learnjava.forkjoin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringTransformExample {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("Bob", "Harry", "Ron", "Adam", "Justin");

        List<String> convertedNames = names.parallelStream() // 1) Fork
                .map(String::toUpperCase) // 2) Process Sequentially to upper case
                .collect(Collectors.toList()); // 3) Join

        System.out.println("Uppercase names: " + convertedNames);
    }
}
