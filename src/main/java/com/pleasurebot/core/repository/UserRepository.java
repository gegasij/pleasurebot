package com.pleasurebot.core.repository;

import com.pleasurebot.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByTelegramId(Long aLong);
    Optional<User> findByLoginAndPassword(String login, String password);
}