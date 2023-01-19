package com.pleasurebot.core.repository;

import com.pleasurebot.core.model.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByTelegramId(Long telegramId);

    @Modifying
    @Query("UPDATE \"user\" SET role = :role WHERE telegram_id = :telegramId")
    boolean updateByFirstName(@Param("role") Long id, @Param("telegramId") String name);
}