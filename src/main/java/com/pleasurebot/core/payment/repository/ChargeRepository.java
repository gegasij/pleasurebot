package com.pleasurebot.core.payment.repository;

import com.pleasurebot.core.payment.model.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChargeRepository extends JpaRepository<Charge, String> {
    List<Charge> findByEmail(String email);
}
