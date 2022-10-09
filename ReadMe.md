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
- Threads have their own runtime-stack, memory, registers and more
