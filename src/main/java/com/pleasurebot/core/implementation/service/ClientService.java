package com.pleasurebot.core.implementation.service;

import com.pleasurebot.core.implementation.model.Client;
import com.pleasurebot.core.implementation.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client getOrCreateClient(Long chatId) {
        return clientRepository.findByChatId(chatId)
                .orElseGet(()->clientRepository.save(Client.builder().chatId(chatId).isPayed(false).build()));
    }
}
