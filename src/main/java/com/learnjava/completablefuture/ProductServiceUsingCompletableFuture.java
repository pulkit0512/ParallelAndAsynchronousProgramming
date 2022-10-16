package com.learnjava.completablefuture;

import com.learnjava.domain.ecommerce.Product;
import com.learnjava.domain.ecommerce.ProductInfo;
import com.learnjava.domain.ecommerce.Review;
import com.learnjava.productservice.ProductInfoService;
import com.learnjava.productservice.ReviewService;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public static void main(String[] args) {
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture();
        productService.setProductInfoService(new ProductInfoService());
        productService.setReviewService(new ReviewService());

        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is: " + product);
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
}
