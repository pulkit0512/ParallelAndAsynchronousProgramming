package com.learnjava.checkoutservice;

import com.learnjava.domain.checkout.CartItem;

import static com.learnjava.util.CommonUtil.delay;

public class PriceValidatorService {

    public boolean isPriceValid(CartItem cartItem) {
        int itemId = cartItem.getItemId();
        delay(500);

        return itemId == 7 || itemId == 9 || itemId == 11;
    }
}
