package com.preeti.sansarcart.service;

import com.preeti.sansarcart.entity.*;
import com.preeti.sansarcart.enums.OrderStatusType;
import com.preeti.sansarcart.repository.AddressRepository;
import com.preeti.sansarcart.repository.CartRepository;
import com.preeti.sansarcart.repository.OrderRepository;
import com.preeti.sansarcart.repository.user.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;



    @Transactional
    public UUID placeOrderFromCart(Customer customer) {

        List<Cart> cartItems = cartRepository.findByCustomer_Id(customer.getId());
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        Address address = customer.getAddresses().get(0);
        // Validate product variations
        for (Cart cart : cartItems) {
            ProductVariation pv = cart.getProductVariation();
            if (Boolean.TRUE.equals(pv.getDeleted()) ||
                    Boolean.FALSE.equals(pv.getActive()) ||
                    pv.getQuantityAvailable() < cart.getQuantity()) {
                throw new IllegalStateException("Product variation " + pv.getId() + " is not available for order");
            }
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Cart cart : cartItems) {
            BigDecimal itemTotal = cart.getProductVariation().getPrice()
                    .multiply(BigDecimal.valueOf(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setAmountPaid(totalAmount.doubleValue());
        order.setDateCreated(LocalDateTime.now());

        order.setCustomerState(address.getState());
        order.setCustomerCountry(address.getCountry());
        order.setCustomerAddressLine(address.getAddressLine());
        order.setCustomerZipCode(address.getZipCode());
        order.setCustomerAddressLabel(address.getLabel());

        List<OrderProduct> orderProducts = new ArrayList<>();

        for (Cart cart : cartItems) {
            ProductVariation pv = cart.getProductVariation();

            pv.setQuantityAvailable(pv.getQuantityAvailable() - cart.getQuantity());

            OrderProduct op = new OrderProduct();
            op.setOrder(order);
            op.setProductVariation(pv);
            op.setQuantity(cart.getQuantity());
            op.setPrice(pv.getPrice());

            OrderStatus initialStatus = new OrderStatus();
            initialStatus.setFromStatus(null);
            initialStatus.setToStatus(OrderStatusType.ORDER_PLACED);
            initialStatus.setTransitionDate(LocalDateTime.now());
            initialStatus.setTransitionNotesComment("Order placed by customer");
            initialStatus.setOrderProduct(op);

            List<OrderStatus> statusList = new ArrayList<>();
            statusList.add(initialStatus);
            orderProducts.add(op);
        }

        order.setOrderProduct(orderProducts);
        orderRepository.save(order);

        cartRepository.deleteAll(cartItems);

        return order.getId();
    }
}
