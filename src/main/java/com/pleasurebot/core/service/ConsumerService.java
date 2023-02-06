package com.pleasurebot.core.service;

import com.pleasurebot.core.model.BasicBundle;
import com.pleasurebot.core.model.BasicBundleConfig;
import com.pleasurebot.core.model.ConsumerBundleResponse;
import com.pleasurebot.core.repository.BundleConfigRepository;
import com.pleasurebot.core.repository.BundleRepository;
import com.pleasurebot.core.repository.SqlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final BundleConfigRepository bundleConfigRepository;
    private final BundleRepository bundleRepository;

    public ConsumerBundleResponse requestBundle(Integer attachedUserId) {
        BasicBundleConfig basicBundleConfig = bundleConfigRepository.getBundleByAttachedUserId(attachedUserId);
        List<BasicBundle> basicBundle = bundleRepository.getBundleByBundleConfig(basicBundleConfig.getId());

        long frequency = basicBundleConfig.getFrequency();
        if (frequency != 0 && isNeedToWaitMore(basicBundleConfig, frequency)) {
            return buildWaitMessage(basicBundleConfig);
        }
        if (basicBundleConfig.isAlwaysNew()) {
            basicBundle = basicBundle.stream()
                    .filter(it -> it.getUsedCount() == 0)
                    .toList();
        }
        if (basicBundle.isEmpty()) {
            return ConsumerBundleResponse.builder().message("сообщения закончились").build();
        }
        if (basicBundleConfig.isRandomOrder()) {
            return ConsumerBundleResponse.builder().basicBundle(basicBundle.get(new Random().nextInt(basicBundle.size()))).build();
        } else {
            return basicBundle.stream()
                    .min(Comparator.comparingInt(BasicBundle::getOrder))
                    .map(it -> ConsumerBundleResponse.builder().basicBundle(it).build())
                    .orElse(ConsumerBundleResponse.builder().message("что-то пошло не так 1").build());
        }
//        return basicBundle.stream()
//                .filter(it -> it.getUsedCount() == 0)
//                .min(Comparator.comparingInt(BasicBundle::getUsedCount))
//                .map(BasicBundle::getUsedCount)
//                .map(it -> basicBundle.stream().filter(bndl -> bndl.getUsedCount() == it).toList())
//                .stream()
//                .flatMap(Collection::stream)
//                .min(Comparator.comparingInt(BasicBundle::getOrder))
//                .map(it -> ConsumerBundleResponse.builder().basicBundle(it).build())
//                .orElse(ConsumerBundleResponse.builder().message("что-то пошло не так 1").build());
    }

    private static ConsumerBundleResponse buildWaitMessage(BasicBundleConfig basicBundleConfig) {
        long l = SqlUtil.secondsBetween(basicBundleConfig.getLastRequestTime(), LocalDateTime.now());
        long l1 = basicBundleConfig.getFrequency() - l;
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plusSeconds(l1);
        long hoursBetween = SqlUtil.hoursBetween(from, to);
        long minutesBetween = SqlUtil.minutesBetween(from, to);
        String waitTime = "%d часов %d мин.".formatted(hoursBetween, minutesBetween - (60 * hoursBetween));
        String message = "Вам осталось ждать %s".formatted(waitTime);
        return ConsumerBundleResponse.builder().message(message).build();
    }

    private static boolean isNeedToWaitMore(BasicBundleConfig basicBundleConfig, long frequency) {
        return basicBundleConfig.getLastRequestTime() != null && frequency > SqlUtil.secondsBetween(basicBundleConfig.getLastRequestTime(), LocalDateTime.now());
    }

}
