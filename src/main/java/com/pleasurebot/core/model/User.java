package com.pleasurebot.core.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Data
public class User {
    @Id
    private int id;
    private long telegramId;
    private String login;
    private String password;
    private Integer role;
    private LocalDateTime creationDate;
}
