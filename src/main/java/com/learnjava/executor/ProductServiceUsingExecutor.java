package com.learnjava.executor;

import com.learnjava.domain.ecommerce.Product;
import com.learnjava.domain.ecommerce.ProductInfo;
import com.learnjava.domain.ecommerce.Review;
import com.learnjava.productservice.ProductInfoService;
import com.learnjava.productservice.ReviewService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.learnjava.util.CommonUtil.noOfCores;
import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingExecutor {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    // Creates an executor service with threads equals to the number of cores in the machine.
    static ExecutorService executorService = Executors.newFixedThreadPool(noOfCores());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try {
            ProductServiceUsingExecutor productService = new ProductServiceUsingExecutor();
            productService.setProductInfoService(new ProductInfoService());
            productService.setReviewService(new ReviewService());

            String productId = "ABC123";
            Product product = productService.retrieveProductDetails(productId);
            log("Product is: " + product);
        } catch(TimeoutException e) {
            log("Timeout Exception: " + e.getMessage());
        } finally {
            // Explicitly we need to shut down the executor service.
            // Else it will keep waiting for the new tasks and will be up and running.
            executorService.shutdown();
        }
    }

    public Product retrieveProductDetails(String productId) throws ExecutionException, InterruptedException, TimeoutException {
        stopWatch.start();

        Future<ProductInfo> productInfoFuture = executorService.submit(() -> productInfoService.getProductInfo(productId));
        Future<Review> reviewFuture = executorService.submit(() -> reviewService.retrieveReviews(productId));

        // We can give a timeout as well to the future, if we get result in this time then good else throw a timeout exception for the service.
        // Issue: Blocks the caller thread until we get result from future. We can time out but still it blocks the caller thread for some time.
        // Futures are designed to block the threads.
        // No better way to combine the futures.
        ProductInfo productInfo = productInfoFuture.get(2, TimeUnit.SECONDS);
        Review review = reviewFuture.get();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }


    public void setProductInfoService(ProductInfoService productInfoService) {
        this.productInfoService = productInfoService;
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }
}
