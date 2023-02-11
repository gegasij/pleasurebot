package com.pleasurebot.core.bot.mapper;

import com.pleasurebot.core.bot.repository.SqlUtil;
import com.pleasurebot.core.bot.model.BasicBundleConfig;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BundleConfigRowMapper implements RowMapper<BasicBundleConfig> {
    @Override
    public BasicBundleConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
        return BasicBundleConfig.builder()
                .id(rs.getInt("id"))
                .ownerUserId(rs.getInt("owner_user_id"))
                .attachedUserId(rs.getInt("attached_user_id"))
                .frequency(rs.getInt("frequency"))
                .isRandomOrder(rs.getBoolean("is_random_order"))
                .isAlwaysNew(rs.getBoolean("is_always_new"))
                .lastRequestTime(SqlUtil.getLocalDateTimeOrNull(rs.getTimestamp("last_request_time")))
                .build();
    }
}
