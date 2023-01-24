package com.pleasurebot.core.repository;

import com.pleasurebot.core.model.Bundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BundlePagingRepository extends JpaRepository<Bundle, Integer> {

}
