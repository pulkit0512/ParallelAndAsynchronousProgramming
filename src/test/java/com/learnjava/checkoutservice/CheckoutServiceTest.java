package com.learnjava.checkoutservice;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;
import com.learnjava.util.DataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
}