package com.pleasurebot.core.controller;

import com.pleasurebot.core.model.User;
import com.pleasurebot.core.repository.UserRepository;
import com.pleasurebot.core.service.DriveBundleAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RequestMapping("/bot")
@RestController
@RequiredArgsConstructor
public class TelegramController {
    private final DriveBundleAdapter driveBundleAdapter;
    private final UserRepository userRepository;

    @GetMapping("update")
    public ResponseEntity<Object> updateBundle() {
        User user= new User();
        user.setTelegramId(new Random().nextInt());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

}
