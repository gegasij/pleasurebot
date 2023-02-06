package com.pleasurebot.core.service.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class PeriodUtil {
    private static final Pair<Long, String> NULL_WAIT = Pair.of(0L, " без задержки");
    private static final Pair<Long, String> MIN_15 = Pair.of(900L, "15 минут");
    private static final Pair<Long, String> HOUR_1 = Pair.of(3600L, "1 час");
    private static final Pair<Long, String> HOUR_3 = Pair.of(10800L, "3 часа");
    private static final Pair<Long, String> DAY_1 = Pair.of(86400L, "1 день");

    private static final CircularList<Pair<Long, String>> periodList = CircularList.of(List.of(
            NULL_WAIT,
            MIN_15,
            HOUR_1,
            HOUR_3,
            DAY_1));
    private static final Map<Long, Pair<Long, String>> periodMap = periodList.getCollection().stream().collect(Collectors.toMap(Pair::getFirst, it -> it));

    public static Pair<Long, String> getNext(Long period) {
        return periodList.getNext(periodMap.getOrDefault(period, DAY_1));
    }
    public static Pair<Long, String> getCurrent(Long period) {
        return periodMap.getOrDefault(period, NULL_WAIT);
    }
}
