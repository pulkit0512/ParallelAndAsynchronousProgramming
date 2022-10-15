package com.learnjava.checkoutservice;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.CommonUtil;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.ForkJoinPool;

import static com.learnjava.util.CommonUtil.noOfCores;
import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    private final PriceValidatorService priceValidatorService = new PriceValidatorService();

    CheckoutService checkoutService = new CheckoutService(priceValidatorService);

    @BeforeEach
    void setUp() {
        stopWatchReset();
    }

    @Test
    void coresAndParallelism() {
        // Parallelism will be no. of system cores - 1
        // the reason behind this is ForkJoinPool involves the thread where you are actually invoking the parallel stream from.
        // In this case we are invoking parallel stream from CheckoutServiceTest class, So it involves the Test Worker thread also as part of the ForkJoinPool.
        // That's why value is no. of cores - 1.
        // The reason for this addition is that we don't end up in a deadlock situation,
        // where a thread waits for a response from the other thread and another thread also waits for response from this thread.
        int noOfCores = CommonUtil.noOfCores();

        int parallelism = ForkJoinPool.getCommonPoolParallelism();

        log("No. of Cores: " + noOfCores);
        log("Parallelism: " + parallelism);

        assertEquals(noOfCores - 1, parallelism);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void checkout(boolean isParallel) {
        Cart cart = DataSet.createCart(6);

        startTimer();
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, isParallel);
        timeTaken();

        assertEquals(CheckoutStatus.SUCCESS, checkoutResponse.getCheckoutStatus());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void checkout_1(boolean isParallel) {
        log("No of Cores: " + noOfCores()); // 8
        // We have 8 cores in our machine, so at a time 8 cartItems can be validated in parallel.
        // So that's why total time is 1000+ milliseconds. Now 3 threads will have delay of 500ms twice.

        Cart cart = DataSet.createCart(noOfCores() + 3);

        startTimer();
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, isParallel);
        timeTaken();

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void checkout25Items() {
        Cart cart = DataSet.createCart(25);

        startTimer();
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, true);
        timeTaken();

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }

    @Test
    void modifyParallelism() {
        // If we change this System property, It will impact the overall CommonForkJoinPool Parallelism for the whole process/app.
        // We can decide this value based on the application requirements.
        // As long as we are not going to invoke a lot of blocking operations in parallel streams go with default value.
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "100");
        Cart cart = DataSet.createCart(100);

        startTimer();
        CheckoutResponse checkoutResponse = checkoutService.checkout(cart, true);
        timeTaken();

        assertEquals(CheckoutStatus.FAILURE, checkoutResponse.getCheckoutStatus());
    }
}