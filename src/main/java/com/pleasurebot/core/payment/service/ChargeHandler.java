package com.pleasurebot.core.payment.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargeHandler {
    private final PaymentService paymentService;
    public String succeeded(String json) {
        Event event = null;

        try {
            Gson gson = new Gson();
            event = gson.fromJson(json, Event.class);
        } catch (JsonSyntaxException e) {
            // Invalid payload
            return "Not an Event Object";
        }

        // With signature verification:
        System.out.println(event.getId());
        System.out.println(event.getType());
        System.out.println(event.getData().getClass());

        // Deserialize the nested object inside the event.
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
        }

        if ("charge.succeeded".equals(event.getType())) {
            Charge charge = (Charge) stripeObject;
            paymentService.handlePayment(charge);
        } else {
            System.out.println("Unhandled event type: " + event.getType());
        }


        return "success";
    }

    public String failed(String json) {
        return null;
    }
}
