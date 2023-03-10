package com.pleasurebot.core.bot.repository;

import com.pleasurebot.core.bot.mapper.BundleRowMapper;
import com.pleasurebot.core.bot.model.BasicBundle;
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

    public List<BasicBundle> getBundleByBundleConfig(Integer bundleConfigId) {
        return namedParameterJdbcTemplate.query(
                "select * from bundle b where b.bundle_config_id = :bundleConfigId",
                Map.of("bundleConfigId", bundleConfigId),
                new BundleRowMapper());
    }

    public void updateLastRequestTime(Integer bundleId, LocalDateTime lastRequestTime) {
        namedParameterJdbcTemplate.update("update bundle " +
                        "set last_request_time = :lastRequestTime, used_count=used_count+1 where" +
                        " id = :bundleId",
                Map.of("bundleId", bundleId, "lastRequestTime", lastRequestTime));
    }
}
