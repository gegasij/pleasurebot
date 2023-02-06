package com.pleasurebot.core.repository;

import com.pleasurebot.core.model.BasicBundleConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BundleConfigJpaRepository extends JpaRepository<BasicBundleConfig, Integer> {
    Optional<BasicBundleConfig> findByOwnerUserId(Integer integer);

}
