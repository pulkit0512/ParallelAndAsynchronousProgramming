# Parallel And Asynchronous Programming in Java

## Why Parallel and Asynchronous Programming ?
We are living in a fast-paced environment. In Software Programming:
- Code that we write should execute faster
- Goal of Asynchronous and Parallel Programming
- Provide Techniques to improve the performance of the code

**Developer needs to learn programming patterns to maximize the use of multiple cores.
Apply the Parallel Programming concepts using Parallel Streams.**

**Blocking I/O calls are common in MicroServices Architecture. This also impacts the latency of the application.
Apply the Asynchronous Programming concepts using CompletableFuture.**

## Concurrency vs Parallelism

![img.png](Concurrency.png)

**In case of Single Core machine, Threads work in Interleaved Fashion. CPU scheduler takes care of which thread to 
execute and when. Whereas in case Multi Core, Threads work simultaneously.**

![img.png](Parallelism.png)

![img.png](ConcurrencyVsParallelism.png)

## Threads API
- Threads API got introduced in Java1.
- Threads are basically used to offload the blocking tasks as background tasks.
- Threads allowed the developers to write asynchronous style of code.

### Thread API Limitations
- Requires a lot of code to introduce asynchronous calling
- Runnable, Thread
- Require additional properties in Runnable
- Start and Join the thread
- Low level
- Easy to introduce complexity in to our code.
- Threads are expensive 
- Threads have their own runtime-stack, memory, registers and more. This is why creating and destroying the threads are expensive operations.

## Thread Pool
- Thread Pool is a group of threads created and readily available
- CPU Intensive Tasks
- ThreadPool Size = No of Cores, because threads will be busy performing the CPU intensive tasks.
- I/O task
- ThreadPool Size > No of Cores, because most of the time, threads will be waiting to get the data from downstream.
- What are the benefits of thread pool?
- No need to manually create, start and join the threads
- Achieving Concurrency in your application

## ExecutorService
- Released as part of Java5
- ExecutorService in Java is an **Asynchronous Task Execution Engine**
- It provides a way to asynchronously execute tasks and provides the results in a much simpler way compared to thread
- This enabled coarse-grained task based parallelism in Java

### Working of an Executor Service
- Executor service has a thread pool, which is a group of threads readily available to execute tasks.
- It also has a Work Queue and Completion Queue.
- Work Queue acts as a blocking queue. All the tasks are placed in the work queue for the thread present in thread pool to pick and execute.
- This returns a Future which is something we can think is like a proxy for which we will get result in later.
- Once the thread completes the execution it places the result in completion queue, and we can get result from this queue using the Future.

![img.png](ExecutorServiceWorking.png)

### Limitations of ExecutorService
- Designed to Block the Thread
- No better way to combine futures

