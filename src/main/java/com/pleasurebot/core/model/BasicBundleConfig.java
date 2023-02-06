package com.pleasurebot.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "bundle_config", schema = "public", catalog = "postgres")
public class BasicBundleConfig {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private Integer ownerUserId;
    private Integer attachedUserId;
    private long frequency;//seconds
    private boolean isRandomOrder;
    private boolean isAlwaysNew;
    private LocalDateTime lastRequestTime;

    @Override
    public String toString() {
        return "BasicBundleConfig{" +
                "frequency=" + frequency +
                ", isRandomRandomOrder=" + isRandomOrder +
                ", isAlwaysNew=" + isAlwaysNew +
                ", lastRequestTime=" + lastRequestTime +
                '}';
    }
}
