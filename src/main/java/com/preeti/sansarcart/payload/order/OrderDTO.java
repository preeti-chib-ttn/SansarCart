package com.preeti.sansarcart.payload.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.preeti.sansarcart.entity.Customer;
import com.preeti.sansarcart.entity.Order;
import com.preeti.sansarcart.enums.AddressLabelType;
import com.preeti.sansarcart.enums.PaymentMethodType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonProperty(value = "customer_id",access = JsonProperty.Access.READ_ONLY)
    private UUID customerId;

    @NotNull(message = "{order.payment.method.required}")
    @JsonProperty("payment_method")
    private PaymentMethodType paymentMethod;

    @JsonProperty("amount_paid")
    private Double amountPaid;

    @JsonProperty("customer_state")
    private String customerState;

    @JsonProperty("customer_country")
    private String customerCountry;

    @JsonProperty("customer_address_line")
    private String customerAddressLine;

    @JsonProperty("customer_zip_code")
    private Long customerZipCode;

    @JsonProperty("customer_address_label")
    private AddressLabelType customerAddressLabel;

    public Order toOrder(Customer customer) {
        Order order = new Order();
        BeanUtils.copyProperties(this, order);
        order.setCustomer(customer);
        order.setDateCreated(LocalDateTime.now());
        return order;
    }

    public static OrderDTO from(Order order) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(order, dto);
        dto.setCustomerId(order.getCustomer() != null ? order.getCustomer().getId() : null);
        return dto;
    }


}
