package com.learnjava.forkjoin;

import com.learnjava.util.DataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

    private List<String> inputNames;

    public ForkJoinUsingRecursion(List<String> inputNames) {
        this.inputNames = inputNames;
    }

    public static void main(String[] args) {
        stopWatch.start();
        List<String> names = DataSet.namesList();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinUsingRecursion forkJoinUsingRecursion = new ForkJoinUsingRecursion(names);

        // Submit task into ForkJoinPool, it takes instance of ForkJoinTask as input.
        // Since ForkJoinUsingRecursion extends RecursiveTask class, ForkJoinUsingRecursion instance will behave as ForkJoinTask.
        // Now the task will be added to the Shared Queue.
        List<String> convertedNames = forkJoinPool.invoke(forkJoinUsingRecursion);

        stopWatch.stop();
        log("Transformed output: " + convertedNames);
        log("Time Taken is: " + stopWatch.getTime());
    }

    private static String addNameLengthTransform(String name) {
        delay(500);
        return name.length() + "-" + name;
    }

    @Override
    protected List<String> compute() {
        if(inputNames.size()<=1){
            // Process the fork join tasks sequentially, using multi-cores for different tasks.
            List<String> result = new ArrayList<>();
            inputNames.forEach(name -> result.add(addNameLengthTransform(name)));
            return result;
        }

        int mid = inputNames.size()/2;
        // Fork the list to sub-tasks, of the least possible data size.
        // Returns a ForkJoinTask
        // .fork() add tasks to worker threads deck.
        // These all are non-blocking tasks and are asynchronously added to worker threads deck
        ForkJoinTask<List<String>> leftInputList = new ForkJoinUsingRecursion(inputNames.subList(0, mid)).fork();
        inputNames = inputNames.subList(mid, inputNames.size());
        List<String> rightResult = compute(); // recursion
        List<String> leftResult = leftInputList.join(); // join the forked result.
        leftResult.addAll(rightResult);

        return leftResult;
    }
}
