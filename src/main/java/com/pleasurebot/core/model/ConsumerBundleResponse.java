package com.pleasurebot.core.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConsumerBundleResponse {
    private Bundle bundle;
    private String message;
}
