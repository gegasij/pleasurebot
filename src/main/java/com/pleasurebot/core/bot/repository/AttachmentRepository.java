package com.pleasurebot.core.bot.repository;

import com.pleasurebot.core.bot.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    List<Attachment> findByBundleId(Integer bundleId);
}
