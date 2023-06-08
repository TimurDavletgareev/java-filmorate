package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Integer filmId;
    @NotNull
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200)
    private final String description;
    @Past
    private final LocalDate releaseDate;
    @Positive
    private final Integer duration;
    private final Integer mpa;
    private Integer rate;
    private Set<Integer> genres;

    // Конструктор для всего, кроме rate и genres
    public Film(Integer filmId, String name, String description, LocalDate releaseDate, Integer duration,
                Integer mpa) {

        this.filmId = filmId;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        rate = 0;
        genres = new HashSet<>();
    }

    // Конструктор для всего, кроме id, rate и genres
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpa) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        rate = 0;
        genres = new HashSet<>();
    }

    // конструктор для всего, кроме id
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpa,
                Integer rate,
                HashSet<Integer> genres) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
        this.genres = genres;
    }

    // конструктор для всего вообще
    public Film(Integer filmId,
                String name, String description, LocalDate releaseDate, Integer duration, Integer mpa,
                Integer rate,
                HashSet<Integer> genres) {

        this.filmId = filmId;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
        this.genres = genres;
    }
}
