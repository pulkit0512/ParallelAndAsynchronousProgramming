package com.learnjava.domain.checkout;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutResponse {
    private CheckoutStatus checkoutStatus;
    private List<CartItem> errorItems;
    private double finalRate;

    public CheckoutResponse(CheckoutStatus checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    public CheckoutResponse(CheckoutStatus checkoutStatus, double finalRate) {
        this.checkoutStatus = checkoutStatus;
        this.finalRate = finalRate;
    }

    public CheckoutResponse(CheckoutStatus checkoutStatus, List<CartItem> errorItems) {
        this.checkoutStatus = checkoutStatus;
        this.errorItems = errorItems;
    }
}
