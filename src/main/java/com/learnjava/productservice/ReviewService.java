package com.learnjava.productservice;

import com.learnjava.domain.ecommerce.Review;

import static com.learnjava.util.CommonUtil.delay;
import static com.learnjava.util.LoggerUtil.log;

public class ReviewService {

    // In real-world, we will fetch this Data from a Database for given productId.
    public Review retrieveReviews(String productId) {
        delay(1000); // to simulate network calls.
        log("Product Id: " + productId);

        return new Review(200, 4.5);
    }
}
