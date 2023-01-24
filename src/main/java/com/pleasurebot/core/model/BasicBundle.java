package com.pleasurebot.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasicBundle {
    private Integer id;
    private Integer bundleConfigId;
    private String message;
    private Integer order;
    private Integer usedCount;
    private LocalDateTime lastRequestTime;
}
