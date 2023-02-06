package com.pleasurebot.core.service.utils;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.LinkedList;

@Getter
@Setter
public class CircularList<T> {
    private CircularList(Collection<T> collection) {
        this.collection = new LinkedList<>(collection);
    }

    public static <T> CircularList<T> of(Collection<T> collection) {
        return new CircularList<>(collection);
    }

    private final LinkedList<T> collection;

    public T getNext(T object) {
        int i = collection.indexOf(object);
        if (i == -1 || i == collection.size() - 1) {
            return collection.getFirst();
        }
        return collection.get(i + 1);
    }
}
