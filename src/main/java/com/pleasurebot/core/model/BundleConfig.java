package com.pleasurebot.core.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Data
@Builder
public class BundleConfig {
    private Integer id;
    private Integer ownerUserId;
    private Integer attachedUserId;
    private long frequency;//seconds
    private boolean isRandomRandomOrder;
    private boolean isAlwaysNew;
    private LocalDateTime lastRequestTime;

    @Override
    public String toString() {
        return "BundleConfig{" +
                "frequency=" + frequency +
                ", isRandomRandomOrder=" + isRandomRandomOrder +
                ", isAlwaysNew=" + isAlwaysNew +
                ", lastRequestTime=" + lastRequestTime +
                '}';
    }
}
