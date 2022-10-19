package com.learnjava.completablefuture;

import com.learnjava.domain.ecommerce.Product;
import com.learnjava.productservice.InventoryService;
import com.learnjava.productservice.ProductInfoService;
import com.learnjava.productservice.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ProductServiceUsingCompletableFutureExceptionTest {

    @Mock
    private ProductInfoService productInfoService;

    @Mock
    private ReviewService reviewService;

    @Mock
    private InventoryService inventoryService;

    private ProductServiceUsingCompletableFuture productServiceUsingCompletableFuture;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        productServiceUsingCompletableFuture = new ProductServiceUsingCompletableFuture();
        productServiceUsingCompletableFuture.setProductInfoService(productInfoService);
        productServiceUsingCompletableFuture.setReviewService(reviewService);
        productServiceUsingCompletableFuture.setInventoryService(inventoryService);
    }

    @Test
    void retrieveProductDetailsWithInventoryWithCompletableFutureRetrieveReviewError() {
        String productId = "ABC123";

        when(productInfoService.getProductInfo(productId)).thenCallRealMethod();
        when(reviewService.retrieveReviews(productId)).thenThrow(new RuntimeException("Exception Occurred"));
        when(inventoryService.retrieveInventory(any())).thenCallRealMethod();

        Product product = productServiceUsingCompletableFuture
                .retrieveProductDetailsWithInventoryWithCompletableFuture(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
        assertEquals(0, product.getReview().getNoOfReviews());
        product.getProductInfo()
                .getProductOptions()
                .forEach(productOption -> assertNotNull(productOption.getInventory()));
    }

    @Test
    void retrieveProductDetailsWithInventoryWithCompletableFutureProductInfoServiceError() {
        String productId = "ABC123";

        when(productInfoService.getProductInfo(productId)).thenThrow(new RuntimeException("Exception Occurred"));
        when(reviewService.retrieveReviews(productId)).thenCallRealMethod();

        Assertions.assertThrows(RuntimeException.class,
                () -> productServiceUsingCompletableFuture.retrieveProductDetailsWithInventoryWithCompletableFuture(productId));
    }

    @Test
    void retrieveProductDetailsWithInventoryWithCompletableFutureInventoryServiceError() {
        String productId = "ABC123";

        when(productInfoService.getProductInfo(productId)).thenCallRealMethod();
        when(reviewService.retrieveReviews(productId)).thenCallRealMethod();
        when(inventoryService.retrieveInventory(any())).thenThrow(new RuntimeException("Exception Occurred"));

        Product product = productServiceUsingCompletableFuture
                .retrieveProductDetailsWithInventoryWithCompletableFuture(productId);

        assertNotNull(product);
        assertTrue(product.getProductInfo().getProductOptions().size()>0);
        assertNotNull(product.getReview());
        assertEquals(1, product.getProductInfo().getProductOptions().get(0).getInventory().getCount());
        product.getProductInfo()
                .getProductOptions()
                .forEach(productOption -> assertNotNull(productOption.getInventory()));
    }
}