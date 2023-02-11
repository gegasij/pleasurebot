package com.pleasurebot.core.payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Charge {
    @Id
    private String id;

    private Long amount;
    private String email;
    private String phone;
    private String name;

}
