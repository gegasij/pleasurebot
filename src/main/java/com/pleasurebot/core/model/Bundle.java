package com.pleasurebot.core.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "BUNDLE", schema = "public", catalog = "postgres")
public class Bundle extends BasicBundle {
    public void setId(Integer id) {
        super.setId(id);
    }
    @Id
    public Integer getId() {
        return super.getId();
    }
}
