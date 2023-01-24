package com.pleasurebot.core.model.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.util.Pair;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class EditMenuMessage extends GenericEditMessage {
    private List<Pair<String, String>> menuList;
}
