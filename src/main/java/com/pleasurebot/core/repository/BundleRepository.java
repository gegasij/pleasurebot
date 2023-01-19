package com.pleasurebot.core.repository;

import com.pleasurebot.core.mapper.BundleConfigRowMapper;
import com.pleasurebot.core.mapper.BundleRowMapper;
import com.pleasurebot.core.model.Bundle;
import com.pleasurebot.core.model.BundleConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BundleRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Bundle> getBundleByBundleConfig(Integer bundleConfigId) {
        return namedParameterJdbcTemplate.query(
                "select * from bundle b where b.bundle_config_id = :bundleConfigId",
                Map.of("bundleConfigId", bundleConfigId),
                new BundleRowMapper());
    }

    public void updateLastRequestTime(Integer bundleId, LocalDateTime lastRequestTime) {
        namedParameterJdbcTemplate.update("update bundle " +
                        "set last_request_time = :lastRequestTime " +
                        "where id = :bundleId",
                Map.of("bundleId", bundleId, "lastRequestTime", lastRequestTime));
    }
}
