package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
    private KVClass mpa;
    private Integer rate;
    private ArrayList<KVClass> genres = new ArrayList<>();


    // конструктор для всего вообще
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                KVClass mpa, Integer rate, ArrayList<KVClass> genres) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
        if (genres != null) {
            this.genres = genres;
        }
    }

    /*

    // Конструктор для всего, кроме genres
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                KVClass mpa, Integer rate) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;

    }


    // Конструктор для всего, кроме genres и rate
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                Integer mpa) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        rate = 0;

    }

    // Конструктор для всего, кроме id, rate и genres
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpa) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        rate = 0;

    }

    // конструктор для всего, кроме id
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Integer mpa,
                Integer rate,
                ArrayList<LinkedHashMap<String, Integer>> genres) {

        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
        this.genres = addGenres(genres);
    }

    // пустой конструктор
    public Film() {
    }
*/
}
