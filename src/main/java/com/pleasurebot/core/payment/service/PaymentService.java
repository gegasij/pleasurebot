package com.pleasurebot.core.payment.service;

import com.pleasurebot.core.payment.mapper.ChargeMapper;
import com.pleasurebot.core.payment.repository.ChargeRepository;
import com.pleasurebot.core.payment.repository.CustomerRepository;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentService {
    private final CustomerRepository customerRepository;
    private final ChargeRepository chargeRepository;

    public void handlePayment(Charge charge){
        //val internCustomer = CustomerMapper.fromStripeCustomer(charge.getCustomerObject());
        val internCharge = ChargeMapper.fromStripeCharge(charge);
        //customerRepository.save(internCustomer);
        chargeRepository.save(internCharge);
    }

}
