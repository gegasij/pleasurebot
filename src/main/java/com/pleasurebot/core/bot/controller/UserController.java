package com.pleasurebot.core.bot.controller;

import com.pleasurebot.core.bot.service.UserService;
import com.pleasurebot.core.bot.model.CreateUserRequest;
import com.pleasurebot.core.bot.model.CreateUserResponse;
import com.pleasurebot.core.bot.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest userRequest) {
        User user = userService.createUser(userRequest.getLogin());
        return Optional.ofNullable(user)
                .map(it -> ResponseEntity.ok(CreateUserResponse.builder().password(user.getPassword()).build()))
                .orElse(ResponseEntity.internalServerError().build());
    }
}
