package com.learnjava.domain.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private int itemId;
    private String itemName;
    private double price;
    private boolean isExpired;
    private int quantity;
}
