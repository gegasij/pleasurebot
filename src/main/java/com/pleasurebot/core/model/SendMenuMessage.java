package com.pleasurebot.core.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@Builder
public class SendMenuMessage {
    private String message;
    private List<Pair<String,String>> menuList;
    private Long chatId;
}
