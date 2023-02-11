package com.pleasurebot.core.bot.mapper;


import com.pleasurebot.core.bot.repository.SqlUtil;
import com.pleasurebot.core.bot.model.BasicBundle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BundleRowMapper implements RowMapper<BasicBundle> {
    @Override
    public BasicBundle mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BasicBundle.builder()
                .id(rs.getInt("id"))
                .bundleConfigId(rs.getInt("bundle_config_id"))
                .message(rs.getString("message"))
                .order(rs.getInt("order"))
                .usedCount(rs.getInt("used_count"))
                .lastRequestTime(SqlUtil.getLocalDateTimeOrNull(rs.getTimestamp("last_request_time")))
                .build();
    }
}