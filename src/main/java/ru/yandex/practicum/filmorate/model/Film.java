package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.relational.core.sql.In;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Film {

    private Integer id;
    @NotNull
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @Past
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private KVClass mpa = new KVClass();
    private Integer rate;
    private Collection genres;

    // Конструктор для всего, кроме genres
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                Integer mpa, Integer rate) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = addMpa(mpa);
        this.rate = rate;
        genres = new HashSet();
    }

    // Конструктор для всего, кроме genres и rate
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                Integer mpa) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = addMpa(mpa);
        rate = 0;
        genres = new HashSet();
    }

    // Конструктор для всего, кроме id, rate и genres
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpa) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = addMpa(mpa);
        rate = 0;
        genres = new HashSet();
    }

    // конструктор для всего, кроме id
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpa,
                Integer rate,
                Collection<Integer> genres) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = addMpa(mpa);
        this.rate = rate;
        this.genres = genres;
    }

    // конструктор для всего вообще
    public Film(Integer id,
                String name, String description, LocalDate releaseDate, Integer duration, Integer mpa,
                Integer rate,
                Collection<Integer> genres) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = addMpa(mpa);
        this.rate = rate;
        this.genres = genres;
    }

    // пустой конструктор
    public Film() {
    }

    /*
        Метод заполнения Mpa
     */
    private KVClass addMpa(Integer mpaId) {

        switch (mpaId) {

            case 1:
                return new KVClass(mpaId, "G");

            case 2:
                return new KVClass(mpaId, "PG");

            case 3:
                return new KVClass(mpaId, "PG-13");

            case 4:
                return new KVClass(mpaId, "R");

            case 5:
                return new KVClass(mpaId, "NC-17");
            default:
                return null;

        }
    }

}
