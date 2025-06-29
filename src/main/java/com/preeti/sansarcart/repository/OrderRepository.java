package com.preeti.sansarcart.repository;

import com.preeti.sansarcart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository  extends JpaRepository<Order, UUID> {
    @Query("SELECT CASE WHEN COUNT(op) > 0 THEN true ELSE false END " +
            "FROM Order o JOIN o.orderProduct op " +
            "WHERE o.customer.id = :customerId AND op.productVariation.product.id = :productId")
    boolean existsByCustomerAndProduct(UUID customerId, UUID productId);
}
