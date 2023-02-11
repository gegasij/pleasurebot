package com.pleasurebot.core.bot.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConsumerBundleResponse {
    private BasicBundle basicBundle;
    private String message;
}
