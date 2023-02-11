package com.pleasurebot.core.payment.service;

import com.pleasurebot.core.bot.model.User;
import com.pleasurebot.core.bot.service.UserService;
import com.pleasurebot.core.payment.repository.ChargeRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivationService {
    private final ChargeRepository chargeRepository;
    private final UserService userService;

    public String activateUser(String login, String text) {
        return chargeRepository.findByEmail(text)
                .stream()
                .findAny()
                .map(it -> userService.createUser(login))
                .map(ActivationService::generateMessage)
                .orElse("с этой почты еще не была произведена оплата");
    }

    @NotNull
    private static String generateMessage(User it) {
        return "Ваш пароль %s. Введите этот пароль боту @myLoveely_bot. Если возникнут вопросы использования бота, можете смело обращаться ко мне в личку @ireut".formatted(it.getPassword());
    }
}
