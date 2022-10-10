package com.learnjava.util;

import java.util.List;

public class DataSet {

    private DataSet() {}

    public static List<String> namesList() {
        return List.of("Bob", "Jamie", "Jill", "Rick", "Harry", "Ron");
    }
}
