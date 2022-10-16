package com.learnjava.domain.ecommerce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOption {
    private int productOptionId;
    private String color;
    private String size;
    private double price;
    private Inventory inventory;

    public ProductOption(int productOptionId, String color, String size, double price) {
        this.productOptionId = productOptionId;
        this.color = color;
        this.size = size;
        this.price = price;
    }
}
