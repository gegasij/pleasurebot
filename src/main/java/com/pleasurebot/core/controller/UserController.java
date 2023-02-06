package com.pleasurebot.core.controller;

import com.pleasurebot.core.model.CreateUserRequest;
import com.pleasurebot.core.model.User;
import com.pleasurebot.core.service.UserService;
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
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest userRequest) {
        User user = userService.createUser(userRequest.getLogin());
        return Optional.ofNullable(user)
                .map(it -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.internalServerError().build());
    }
}
