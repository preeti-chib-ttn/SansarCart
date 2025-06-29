package com.preeti.sansarcart.controller;

import com.preeti.sansarcart.entity.Customer;
import com.preeti.sansarcart.entity.User;
import com.preeti.sansarcart.payload.product.review.ProductReviewDTO;
import com.preeti.sansarcart.response.ApiResponse;
import com.preeti.sansarcart.service.AuthenticationService;
import com.preeti.sansarcart.service.ProductReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.preeti.sansarcart.common.I18n.i18n;

@RestController
@RequestMapping("/product/review")
@RequiredArgsConstructor
@Slf4j
//check for customer
public class ProductReviewController {

    private final ProductReviewService productReviewService;
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductReviewDTO>> createReview(
            @Valid @RequestBody ProductReviewDTO reviewDTO) {

        Customer currentUser =(Customer) authenticationService.getCurrentUser();
        log.info("Attempting to create review for user: {}", currentUser.getId());

        ProductReviewDTO created = productReviewService.createProductReview(
                currentUser, reviewDTO);

        log.info("Review created successfully with ID: {}", created.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(i18n("review.create.success"), created));
    }
}
