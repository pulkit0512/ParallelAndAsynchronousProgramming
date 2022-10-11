package com.learnjava.checkoutservice;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;
import com.learnjava.domain.checkout.CheckoutResponse;
import com.learnjava.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckoutService {
    private final PriceValidatorService priceValidatorService;

    public CheckoutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }

    public CheckoutResponse checkout(Cart cart, boolean isParallel) {

        Stream<CartItem> cartStream = cart.getCartItems().stream();

        if(isParallel) {
            cartStream = cartStream.parallel();
        }


        List<CartItem> priceValidatorList = cartStream
                // peek takes in a Consumer and apply the provided action on each element
                // as elements are consumed from the input stream
                // map transforms the stream and returns a new Stream of transformed object
                // peek takes in a consumer and applies required operations on the input stream
                // this is useful when input and output stream types are same.
                // peek is mainly used for debugging purpose.
                .map(cartItem -> {
                    boolean isExpired = priceValidatorService.isPriceValid(cartItem);
                    cartItem.setExpired(isExpired);
                    return cartItem;
                })
                .filter(CartItem::isExpired)
                .collect(Collectors.toList());

        if(!priceValidatorList.isEmpty()) {
            return new CheckoutResponse(CheckoutStatus.FAILURE, priceValidatorList);
        }else{
            return new CheckoutResponse(CheckoutStatus.SUCCESS);
        }
    }
}
