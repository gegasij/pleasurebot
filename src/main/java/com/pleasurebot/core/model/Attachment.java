package com.pleasurebot.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Attachment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "bundle_id")
    private Integer bundleId;
    @Column(name = "telegram_file_id")
    private String telegramFileId;

    @Column(name = "attachment_type")
    private Integer attachmentType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(id, that.id) && Objects.equals(bundleId, that.bundleId) && Objects.equals(telegramFileId, that.telegramFileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bundleId, telegramFileId);
    }
}
