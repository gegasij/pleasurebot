package com.pleasurebot.core.implementation.repository;

import com.pleasurebot.core.implementation.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByChatId(Long chatId);
}
