package com.pleasurebot.core.bot.service;

import com.pleasurebot.core.bot.model.BasicBundle;
import com.pleasurebot.core.bot.model.BasicBundleConfig;
import com.pleasurebot.core.bot.repository.BundleConfigJpaRepository;
import com.pleasurebot.core.bot.repository.BundleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BundleService {
    private final BundleJpaRepository bundleJpaRepository;
    private final BundleConfigJpaRepository bundleConfigJpaRepository;


    public Integer getNextOrderNumber(Integer bundleConfigId) {

        return bundleJpaRepository.countByBundleConfigId(bundleConfigId) + 1;
    }

    public void deleteBundle(Integer bundleId) {
        Optional<BasicBundle> bundle = bundleJpaRepository.findById(bundleId);
        if (bundle.isPresent()) {
            Optional<BasicBundleConfig> bundleConfig = bundleConfigJpaRepository.findById(bundle.get().getBundleConfigId());
            if (bundleConfig.isPresent()) {
                List<BasicBundle> order = bundleJpaRepository.findAllByBundleConfigId(bundleConfig.get().getId(), Sort.by("order"));

                order.stream()
                        .filter(it -> it.getId().equals(bundleId))
                        .peek(it -> bundleJpaRepository.deleteById(it.getId()))
                        .findFirst()
                        .ifPresent(order::remove);

                int i = 1;
                for (BasicBundle bundle1 : order) {
                    bundle1.setOrder(i);
                    i++;
                }
                bundleJpaRepository.saveAll(order);
            }
        }
    }
}
