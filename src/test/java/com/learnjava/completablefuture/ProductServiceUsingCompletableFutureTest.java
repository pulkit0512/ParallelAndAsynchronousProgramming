package com.learnjava.completablefuture;

import com.learnjava.domain.ecommerce.Product;
import com.learnjava.productservice.ProductInfoService;
import com.learnjava.productservice.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.learnjava.util.CommonUtil.stopWatchReset;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {
    private final ProductInfoService productInfoService = new ProductInfoService();
    private final ReviewService reviewService = new ReviewService();

    private final ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture();

    @BeforeEach
    void setUp() {
        productService.setProductInfoService(productInfoService);
        productService.setReviewService(reviewService);
        stopWatchReset();
    }

    @Test
    void retrieveProductDetails() {
        String productId = "PA0512";

        Product product = productService.retrieveProductDetails(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
    }

    @Test
    void retrieveProductDetailsAsServer() {
        String productId = "PA0512";

        CompletableFuture<Product> productFuture = productService.retrieveProductDetailsAsServer(productId);

        assertEquals(productId, productFuture.join().getProductId());
    }
}