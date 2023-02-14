package com.pleasurebot.core.implementation.service;

import com.pleasurebot.core.implementation.model.Client;
import com.pleasurebot.core.implementation.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Client getOrCreateClient(Long chatId, String telegramUsername) {
        return clientRepository.findByChatId(chatId)
                .orElseGet(() -> clientRepository.save(Client.builder()
                        .chatId(chatId)
                        .username(telegramUsername)
                        .isPayed(false)
                        .build()));
    }
}
