package com.learnjava.productservice;

import com.learnjava.domain.ecommerce.Product;
import com.learnjava.domain.ecommerce.ProductInfo;
import com.learnjava.domain.ecommerce.Review;

import static com.learnjava.util.CommonUtil.stopWatch;
import static com.learnjava.util.LoggerUtil.log;

public class ProductService {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public static void main(String[] args) {
        ProductService productService = new ProductService();
        productService.setProductInfoService(new ProductInfoService());
        productService.setReviewService(new ReviewService());

        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is: " + product);
    }

    public Product retrieveProductDetails(String productId) {
        stopWatch.start();

        // Pros: 1) Intuitive and Synchronous code.
        // Cons: 2) Blocking nature, meaning review service will only be called once we get results for the productInfo.
        // even though review service is independent of the productInfo service.
        ProductInfo productInfo = productInfoService.getProductInfo(productId); // blocking call
        Review review = reviewService.retrieveReviews(productId); // blocking call

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
