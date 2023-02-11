package com.pleasurebot.core.bot.service;

import com.pleasurebot.core.bot.model.BasicBundleConfig;
import com.pleasurebot.core.bot.model.Role;
import com.pleasurebot.core.bot.model.User;
import com.pleasurebot.core.bot.model.BotState;
import com.pleasurebot.core.bot.repository.BundleConfigJpaRepository;
import com.pleasurebot.core.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BundleConfigService {
    private static final long DEFAULT_FREQUENCY = 1500;
    private static final boolean DEFAULT_RANDOM_ORDER = true;
    private static final boolean DEFAULT_IS_ALWAYS_NEW = true;
    private final BundleConfigJpaRepository bundleConfigJpaRepository;
    private final UserRepository userRepository;

    public BasicBundleConfig createDefault(Integer ownerUserId) {
        User attachedUser = userRepository.save(User.builder()
                .botState(BotState.NONE.getValue())
                .creationDate(LocalDateTime.now())
                .role(Role.CONSUMER.getRoleId())
                .password(UserService.generatePassword())
                .build());

        return bundleConfigJpaRepository.save(BasicBundleConfig.builder()
                .frequency(DEFAULT_FREQUENCY)
                .isRandomOrder(DEFAULT_RANDOM_ORDER)
                .isAlwaysNew(DEFAULT_IS_ALWAYS_NEW)
                .ownerUserId(ownerUserId)
                .attachedUserId(attachedUser.getId())
                .build());
    }

    public BasicBundleConfig getBasicBundleConfig(Integer ownerId) {
        return bundleConfigJpaRepository.findByOwnerUserId(ownerId)
                .orElseGet(() -> createDefault(ownerId));
    }
}
