package com.pleasurebot.core.mapper;


import com.pleasurebot.core.model.Bundle;
import com.pleasurebot.core.model.BundleConfig;
import com.pleasurebot.core.repository.SqlUtil;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BundleRowMapper implements RowMapper<Bundle> {
    @Override
    public Bundle mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Bundle.builder()
                .id(rs.getInt("id"))
                .bundleConfigId(rs.getInt("bundle_config_id"))
                .message(rs.getString("message"))
                .order(rs.getInt("order"))
                .usedCount(rs.getInt("used_count"))
                .lastRequestTime(SqlUtil.getLocalDateTimeOrNull(rs.getTimestamp("last_request_time")))
                .build();
    }
}