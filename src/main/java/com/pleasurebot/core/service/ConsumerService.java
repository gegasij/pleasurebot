package com.pleasurebot.core.service;

import com.pleasurebot.core.model.Bundle;
import com.pleasurebot.core.model.BundleConfig;
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
        BundleConfig bundleConfig = bundleConfigRepository.getBundleByAttachedUserId(attachedUserId);
        List<Bundle> bundle = bundleRepository.getBundleByBundleConfig(bundleConfig.getId());

        long frequency = bundleConfig.getFrequency();
        if (frequency != 0 && isNeedToWaitMore(bundleConfig, frequency)) {
            return ConsumerBundleResponse.builder().message("Еще не время").build();
        }
        if (bundleConfig.isAlwaysNew()) {
            boolean isNoBundleUsedZeroTimes = bundle.stream()
                    .anyMatch(it -> it.getUsedCount().equals(0));
            if (isNoBundleUsedZeroTimes) {
                return ConsumerBundleResponse.builder().message("сообщения закончились").build();
            }
        }
        if (bundleConfig.isRandomRandomOrder()) {
            if (!bundleConfig.isAlwaysNew()) {
                return ConsumerBundleResponse.builder().bundle(bundle.get(new Random().nextInt(bundle.size()))).build();
            } else {
                return bundle.stream()
                        .filter(it -> it.getUsedCount().equals(0))
                        .min(Comparator.comparingInt(Bundle::getOrder))
                        .map(it -> ConsumerBundleResponse.builder().bundle(it).build())
                        .orElse(ConsumerBundleResponse.builder().message("что-то пошло не так 1").build());
            }
        }
        return bundle.stream()
                .filter(it -> it.getUsedCount().equals(0))
                .min(Comparator.comparingInt(Bundle::getUsedCount))
                .map(Bundle::getUsedCount)
                .map(it -> bundle.stream().filter(bndl -> bndl.getUsedCount().equals(it)).toList())
                .stream()
                .flatMap(Collection::stream)
                .min(Comparator.comparingInt(Bundle::getOrder))
                .map(it -> ConsumerBundleResponse.builder().bundle(it).build())
                .orElse(ConsumerBundleResponse.builder().message("что-то пошло не так 1").build());
    }

    private static boolean isNeedToWaitMore(BundleConfig bundleConfig, long frequency) {
        return bundleConfig.getLastRequestTime() != null && frequency > SqlUtil.secondsBetween(bundleConfig.getLastRequestTime(), LocalDateTime.now());
    }

}
