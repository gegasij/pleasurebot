package com.pleasurebot.core.bot.repository;

import com.pleasurebot.core.bot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByTelegramId(Long aLong);
    Optional<User> findByLoginAndPassword(String login, String password);
    Optional<User> findByLogin(String login);
}