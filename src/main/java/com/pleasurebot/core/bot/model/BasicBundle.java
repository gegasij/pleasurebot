package com.pleasurebot.core.bot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "Bundle", schema = "public", catalog = "postgres")
public class BasicBundle {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private Integer bundleConfigId;
    private String message;
    @Column(name = "\"order\"")
    private Integer order;
    @Column(name = "used_count")
    @Basic
    private int usedCount;
    private LocalDateTime lastRequestTime;
}
