package com.pleasurebot.core.service;

import com.pleasurebot.core.model.User;
import com.pleasurebot.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final short GENERATING_NAME_LENGTH = 7;

    public User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(generatePassword());
        userRepository.save(user);
        return user;
    }

    private String generatePassword() {
        byte[] array = new byte[GENERATING_NAME_LENGTH]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
