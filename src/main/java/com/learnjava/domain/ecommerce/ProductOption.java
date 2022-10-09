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
}
