package com.pleasurebot.core.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Bundle {
    private Integer id;
    private Integer bundleConfigId;
    private String message;
    private Integer order;
    private Integer usedCount;
    private LocalDateTime lastRequestTime;
}
