package com.preeti.sansarcart.service;

import com.preeti.sansarcart.entity.Cart;
import com.preeti.sansarcart.entity.Customer;
import com.preeti.sansarcart.entity.ProductVariation;
import com.preeti.sansarcart.exception.custom.ResourceNotFound;
import com.preeti.sansarcart.exception.custom.ValidationException;
import com.preeti.sansarcart.payload.product.cart.CartDTO;
import com.preeti.sansarcart.repository.CartRepository;
import com.preeti.sansarcart.repository.ProductVariationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.preeti.sansarcart.common.I18n.i18n;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductVariationRepository productVariationRepository;
    private final CartRepository cartRepository;

    @Transactional
    public CartDTO addToCart(CartDTO cartDTO, Customer customer) {

        ProductVariation variation = productVariationRepository.findById(cartDTO.getProductVariationId())
                .orElseThrow(() -> new ResourceNotFound(i18n("exception.product.variation.not.found")));

        if (!variation.getActive()) {
            throw new ValidationException(i18n("validation.product.variation.not.active"));
        }
        // check for existing value
        Cart cart = cartDTO.toCart(customer, variation);

        Cart saved = cartRepository.save(cart);

        return CartDTO.from(saved);
    }
}
