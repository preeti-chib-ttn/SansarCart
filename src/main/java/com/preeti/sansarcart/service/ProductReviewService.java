package com.preeti.sansarcart.service;

import com.preeti.sansarcart.entity.Customer;
import com.preeti.sansarcart.entity.Product;
import com.preeti.sansarcart.entity.ProductReview;
import com.preeti.sansarcart.exception.custom.ResourceNotFound;
import com.preeti.sansarcart.exception.custom.ValidationException;
import com.preeti.sansarcart.payload.product.review.ProductReviewDTO;
import com.preeti.sansarcart.repository.OrderRepository;
import com.preeti.sansarcart.repository.ProductRepository;
import com.preeti.sansarcart.repository.ProductReviewRepository;
import com.preeti.sansarcart.service.sentimental.analysis.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.preeti.sansarcart.common.I18n.i18n;


@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final OrderRepository orderRepository;
    private final SentimentAnalysisService sentimentAnalysisService;

    @Transactional
    public ProductReviewDTO createProductReview(Customer customer, ProductReviewDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFound(i18n("exception.product.not.found")));

        boolean hasOrdered = orderRepository.existsByCustomerAndProduct(customer.getId(), product.getId());

        if (!hasOrdered) {
            throw new ValidationException(i18n("validation.review.product.not.purchased"));
        }

        ProductReview review = dto.toEntity(customer, product);
        ProductReview savedReview = productReviewRepository.save(review);
        return ProductReviewDTO.from(savedReview);
    }

}
