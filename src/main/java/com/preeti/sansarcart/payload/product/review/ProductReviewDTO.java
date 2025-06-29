package com.preeti.sansarcart.payload.product.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.preeti.sansarcart.entity.Customer;
import com.preeti.sansarcart.entity.Product;
import com.preeti.sansarcart.entity.ProductReview;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Getter @Setter
public class ProductReviewDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotNull(message = "{product.review.productId.required}")
    @JsonProperty("product_id")
    private UUID productId;

    @NotBlank(message = "{product.review.text.required}")
    private String review;

    @NotNull
    @Min(value = 1, 
            message = "{product.review.rating.min}")
    @Max(value = 5, message = "{product.review.rating.max}")
    private Integer rating;

    @JsonProperty(value = "customer_id", access = JsonProperty.Access.READ_ONLY)
    private UUID customerId;

    public ProductReview toEntity(Customer customer, Product product) {
        ProductReview entity = new ProductReview();
        BeanUtils.copyProperties(this, entity);
        entity.setCustomer(customer);
        entity.setProduct(product);
        return entity;
    }

    public static ProductReviewDTO from(ProductReview review) {
        ProductReviewDTO dto = new ProductReviewDTO();
        BeanUtils.copyProperties(review, dto);
        dto.setCustomerId(review.getCustomer().getId());
        dto.setProductId(review.getProduct().getId());
        return dto;
    }

}
