package com.learnjava.domain.ecommerce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data // takes care of all the getter setters.
@NoArgsConstructor // creates a constructor with no arguments
@AllArgsConstructor // creates a constructor with all data members
@Builder // Used to build the data object
public class ProductInfo {
    String productId;
    List<ProductOption> productOptions;
}
