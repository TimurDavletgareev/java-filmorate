package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class KVClass {

    private Integer id;
    private String name;


    public KVClass(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
