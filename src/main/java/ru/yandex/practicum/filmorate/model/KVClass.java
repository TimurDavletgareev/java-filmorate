package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.relational.core.sql.In;

@Data
public class KVClass {

    private Integer id;
    private String name;

    public KVClass(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public KVClass() {};

}
