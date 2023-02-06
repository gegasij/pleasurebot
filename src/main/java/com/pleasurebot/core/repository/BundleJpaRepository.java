package com.pleasurebot.core.repository;

import com.pleasurebot.core.model.BasicBundle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BundleJpaRepository extends JpaRepository<BasicBundle, Integer> {
    List<BasicBundle> findAllByBundleConfigId(Integer bundleConfigId, Sort sort);
    Integer countByBundleConfigId(Integer bundleConfigId);
}
