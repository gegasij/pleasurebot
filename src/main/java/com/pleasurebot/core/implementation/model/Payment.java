package com.pleasurebot.core.implementation.model;

import com.pleasurebot.core.implementation.model.Invoice;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Payment {
    private String providerToken;
    private List<Invoice> invoice;
}
