package com.pleasurebot.core.bot.repository;

import com.pleasurebot.core.bot.mapper.BundleConfigRowMapper;
import com.pleasurebot.core.bot.model.BasicBundleConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BundleConfigRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void updateLastRequestTime(Integer attachedUserId) {
        namedParameterJdbcTemplate.update("update Bundle_Config bc " +
                        "set last_request_time = now() where" +
                        " attached_user_id   = :attachedUserId",
                Map.of("attachedUserId", attachedUserId));
    }

    public BasicBundleConfig getBundleByAttachedUserId(Integer attachedUserId) {
        return namedParameterJdbcTemplate.queryForObject(
                "select * from Bundle_Config bc where bc.attached_user_id = :attachedUserId",
                Map.of("attachedUserId", attachedUserId),
                new BundleConfigRowMapper());
    }
}
