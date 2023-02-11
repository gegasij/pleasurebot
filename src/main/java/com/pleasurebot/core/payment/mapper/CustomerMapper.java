package com.pleasurebot.core.payment.mapper;

import com.pleasurebot.core.payment.model.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerMapper {
    public Customer fromStripeCustomer(com.stripe.model.Customer customer) {
        return Customer.builder()
                .id(customer.getId())
                .amount(null)
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .name(customer.getName())
                .build();
    }
}
