package com.learnjava.productservice;

import com.learnjava.domain.ecommerce.Inventory;
import com.learnjava.domain.ecommerce.ProductOption;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.LoggerUtil.log;

public class InventoryService {

    public Inventory retrieveInventory(ProductOption productOption) {
        delay(500);
        log(String.valueOf(productOption.getProductOptionId()));

        return Inventory
                .builder()
                .count(2)
                .build();
    }

    public CompletableFuture<Inventory> retrieveInventoryCompletableFuture(ProductOption productOption) {
        log(productOption.toString());

        return CompletableFuture.supplyAsync(() -> {
            delay(500);
            return Inventory.builder()
                    .count(2).build();
        });

    }
}
