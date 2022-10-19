package com.learnjava.completablefuture;

import com.learnjava.domain.ecommerce.Inventory;
import com.learnjava.domain.ecommerce.Product;
import com.learnjava.domain.ecommerce.ProductInfo;
import com.learnjava.domain.ecommerce.ProductOption;
import com.learnjava.domain.ecommerce.Review;
import com.learnjava.productservice.InventoryService;
import com.learnjava.productservice.ProductInfoService;
import com.learnjava.productservice.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.stopWatchReset;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;
    private InventoryService inventoryService;

    public static void main(String[] args) {
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture();
        productService.setProductInfoService(new ProductInfoService());
        productService.setReviewService(new ReviewService());
        productService.setInventoryService(new InventoryService());

        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is: " + product);

        Product product1 = productService.retrieveProductDetailsWithInventory(productId);
        log("Product with Inventory: " + product1);
    }

    public Product retrieveProductDetails(String productId) {
        startTimer();

        CompletableFuture<ProductInfo> productInfoFuture = CompletableFuture
                .supplyAsync(() -> productInfoService.getProductInfo(productId));

        CompletableFuture<Review> reviewFuture = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId));

        // If building as a client then it's our need to retrieve the value from the completableFuture using join
        // which blocks the thread.
        Product product = productInfoFuture
                .thenCombine(reviewFuture, (productInfo, review) -> new Product(productId, productInfo, review))
                .join(); // blocks the thread

        timeTaken();
        return product;
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatchReset();
        startTimer();

        // As we increase the number of product options, the performance degrades.
        // Since Inventory service is dependent on productInfo service so these are blocking calls.
        CompletableFuture<ProductInfo> productInfoFuture = CompletableFuture
                .supplyAsync(() -> productInfoService.getProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventory(productInfo));
                    return productInfo;
                });

        CompletableFuture<Review> reviewFuture = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId));

        Product product = productInfoFuture
                .thenCombine(reviewFuture, (productInfo, review) -> new Product(productId, productInfo, review))
                .join(); // blocks the thread

        timeTaken();
        return product;
    }

    public Product retrieveProductDetailsWithInventoryWithCompletableFuture(String productId) {
        stopWatchReset();
        startTimer();

        // As we increase the number of product options, the performance remains almost same.
        // Since Inventory for each product option is updated in parallel. So only 500ms time is needed to update inventory for both product option
        // as compared to 1sec which was needed in the above approach.

        // Total time will be now 1.5sec
        // 1sec for productInfoService and 0.5sec for Inventory service which executes only after product info service call is completed.

        // In case of nested blocking calls better to use this approach
        CompletableFuture<ProductInfo> productInfoFuture = CompletableFuture
                .supplyAsync(() -> productInfoService.getProductInfo(productId))
                .thenApply(productInfo -> {
                    productInfo.setProductOptions(updateInventoryCompletableFuture(productInfo));
                    return productInfo;
                });

        CompletableFuture<Review> reviewFuture = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId))
                .exceptionally(ex -> {
                    log("Handled exception occurred in Review service: " + ex.getMessage());
                    return Review.builder()
                            .noOfReviews(0)
                            .rating(0.0)
                            .build();
                });

        Product product = productInfoFuture
                .thenCombine(reviewFuture, (productInfo, review) -> new Product(productId, productInfo, review))
                .whenComplete((pro, ex) -> {
                    if(ex!=null) {
                        log("Inside whenComplete : " + pro + " Exception is: " + ex.getMessage());
                    }
                })
                .join(); // blocks the thread

        timeTaken();
        return product;
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        return productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    Inventory inventory = inventoryService.retrieveInventory(productOption); // blocking call
                    productOption.setInventory(inventory);
                    return productOption;
                })
                .collect(Collectors.toList());
    }

    private List<ProductOption> updateInventoryCompletableFuture(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> completableFutureList = productInfo.getProductOptions()
                .stream()
                .map(productOption ->
                        CompletableFuture.supplyAsync(() -> inventoryService.retrieveInventory(productOption))
                        .exceptionally(ex -> {
                            log("In Exceptionally block of Inventory Service: " + ex.getMessage());
                            return Inventory.builder().count(1).build();
                        })
                        .thenApply(inventory -> {
                            productOption.setInventory(inventory);
                            return productOption;
                        }))
                .collect(Collectors.toList());

        return completableFutureList.stream()
                .map(CompletableFuture::join) // These join on each completable future are async calls only.
                .collect(Collectors.toList());
    }

    public CompletableFuture<Product> retrieveProductDetailsAsServer(String productId) {

        CompletableFuture<ProductInfo> productInfoFuture = CompletableFuture
                .supplyAsync(() -> productInfoService.getProductInfo(productId));

        CompletableFuture<Review> reviewFuture = CompletableFuture
                .supplyAsync(() -> reviewService.retrieveReviews(productId));

        // In case building for a server, server can return the CompletableFuture.
        // It will be the responsibility of the client on how to use this CompletableFuture.
        return productInfoFuture
                .thenCombine(reviewFuture, (productInfo, review) -> new Product(productId, productInfo, review));
    }


    public void setProductInfoService(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
}
