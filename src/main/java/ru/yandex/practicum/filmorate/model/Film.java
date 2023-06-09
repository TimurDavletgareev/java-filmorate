package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;

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
    private HashSet<Integer> genres;

    // Конструктор для всего, кроме rate и genres
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                Integer mpa) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa.setId(mpa);
        rate = 0;
        genres = new HashSet<>();
    }

    // Конструктор для всего, кроме id, rate и genres
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpa) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa.setId(mpa);
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
        this.mpa.setId(mpa);
        this.rate = rate;
        this.genres = genres;
    }

    // конструктор для всего вообще
    public Film(Integer id,
                String name, String description, LocalDate releaseDate, Integer duration, Integer mpa,
                Integer rate,
                HashSet<Integer> genres) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa.setId(mpa);
        this.rate = rate;
        this.genres = genres;
    }

    public Film() {}
}
