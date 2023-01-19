package com.pleasurebot.core.repository;

import com.pleasurebot.core.mapper.BundleConfigRowMapper;
import com.pleasurebot.core.model.BundleConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BundleConfigRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<BundleConfig> getBundleByOwnerId(Integer ownerUserId) {
        return namedParameterJdbcTemplate.query(
                "select * from Bundle_Config bc where bc.owner_user_id = :ownerUserId",
                Map.of("ownerUserId", ownerUserId),
                new BundleConfigRowMapper());
    }
    public BundleConfig getBundleByAttachedUserId(Integer attachedUserId) {
        return namedParameterJdbcTemplate.queryForObject(
                "select * from Bundle_Config bc where bc.attached_user_id = :attachedUserId",
                Map.of("attachedUserId", attachedUserId),
                new BundleConfigRowMapper());
    }
}
