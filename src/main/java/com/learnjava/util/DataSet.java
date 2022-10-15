package com.learnjava.util;

import com.learnjava.domain.checkout.Cart;
import com.learnjava.domain.checkout.CartItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class DataSet {

    private DataSet() {}

    public static List<String> namesList() {
        return List.of("Bob", "Jamie", "Jill", "Rick", "Harry", "Ron");
    }

    public static Cart createCart(int noOfItems) {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        IntStream.rangeClosed(1, noOfItems)
                .forEach(index -> {
                    String itemName = "CartItem-" + index;
                    CartItem cartItem = new CartItem(index, itemName, generateRandomPrice(), false, index);
                    cartItems.add(cartItem);
                });

        cart.setCartItems(cartItems);
        cart.setCartId(1);
        return cart;
    }

    public static double generateRandomPrice() {
        int min = 50;
        int max = 100;
        return Math.random() * (max - min + 1) + min;
    }

    public static ArrayList<Integer> generateArrayList(int size) {
        ArrayList<Integer> arrayList = new ArrayList<>();

        IntStream.rangeClosed(1, size)
                .boxed()
                .forEach(arrayList :: add);

        return arrayList;
    }

    public static LinkedList<Integer> generateLinkedList(int size) {
        LinkedList<Integer> linkedList = new LinkedList<>();

        IntStream.rangeClosed(1, size)
                .boxed()
                .forEach(linkedList :: add);

        return linkedList;
    }
}
