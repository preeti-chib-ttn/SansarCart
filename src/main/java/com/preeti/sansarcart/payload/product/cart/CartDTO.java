package com.preeti.sansarcart.payload.product.cart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.preeti.sansarcart.entity.Cart;
import com.preeti.sansarcart.entity.CartId;
import com.preeti.sansarcart.entity.Customer;
import com.preeti.sansarcart.entity.ProductVariation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartDTO {

    @JsonProperty(value = "customer_id", access = JsonProperty.Access.READ_ONLY)
    private UUID customerId;

    @NotNull(message = "{cart.product.variation.id.required}")
    @JsonProperty("product_variation_id")
    private UUID productVariationId;

    @NotNull(message = "{cart.quantity.required}")
    @Min(value = 1, message = "{cart.quantity.min}")
    @JsonProperty("quantity")
    private Long quantity;

    @JsonProperty("is_wishlist_item")
    private Boolean isWishListItem = false;

    public static CartDTO from(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setCustomerId(cart.getCustomer().getId());
        dto.setProductVariationId(cart.getProductVariation().getId());
        dto.setQuantity(cart.getQuantity());
        dto.setIsWishListItem(cart.getIsWishListItem());
        return dto;
    }

    public Cart toCart(Customer customer, ProductVariation productVariation) {
        Cart cart = new Cart();
        cart.setId(new CartId(customer.getId(), productVariation.getId()));
        cart.setCustomer(customer);
        cart.setProductVariation(productVariation);
        cart.setQuantity(this.quantity);
        cart.setIsWishListItem(this.isWishListItem != null ? this.isWishListItem : false);
        return cart;
    }
}
