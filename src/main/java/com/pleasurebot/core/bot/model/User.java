package com.pleasurebot.core.bot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "user", schema = "public", catalog = "postgres")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "telegram_id")
    private Long telegramId;
    @Column(name = "bot_state")
    private Integer botState;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private Integer role;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
