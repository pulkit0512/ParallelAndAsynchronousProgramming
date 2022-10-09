package com.learnjava.thread;

import com.learnjava.domain.ecommerce.Product;
import com.learnjava.domain.ecommerce.ProductInfo;
import com.learnjava.domain.ecommerce.Review;
import com.learnjava.productservice.ProductInfoService;
import com.learnjava.productservice.ReviewService;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductServiceUsingThread {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    // Thread API, is very low level API. To easy to introduce complexity in the code while performing asynchronous operations.
    // Not developer friendly.
    public static void main(String[] args) throws InterruptedException {
        ProductServiceUsingThread productService = new ProductServiceUsingThread();
        productService.setProductInfoService(new ProductInfoService());
        productService.setReviewService(new ReviewService());

        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is: " + product);
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        stopWatch.start();

        ProductInfoRunnable productInfoRunnable = new ProductInfoRunnable(productId);
        ReviewRunnable reviewRunnable = new ReviewRunnable(productId);

        Thread productInfoThread = new Thread(productInfoRunnable);
        Thread reviewThread = new Thread(reviewRunnable);

        // Starts the thread
        productInfoThread.start();
        reviewThread.start();

        // Join the thread, so that it waits until execution completes.
        productInfoThread.join();
        reviewThread.join();

        ProductInfo productInfo = productInfoRunnable.getProductInfo();
        Review review = reviewRunnable.getReview();

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

    private class ProductInfoRunnable implements Runnable {
        private final String productId;
        private ProductInfo productInfo;

        public ProductInfoRunnable(String productId) {
            this.productId = productId;
        }

        public ProductInfo getProductInfo() {
            return productInfo;
        }

        @Override
        public void run() {
            productInfo = productInfoService.getProductInfo(productId);
        }
    }

    private class ReviewRunnable implements Runnable {
        private final String productId;
        private Review review;

        public ReviewRunnable(String productId) {
            this.productId = productId;
        }

        public Review getReview() {
            return review;
        }

        @Override
        public void run() {
            review = reviewService.retrieveReviews(productId);
        }
    }
}
