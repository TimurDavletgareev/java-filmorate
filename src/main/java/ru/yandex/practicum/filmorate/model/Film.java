package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.relational.core.sql.In;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

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
    private Integer mpa;
    private Integer rate;
    private ArrayList<Integer> genres = new ArrayList<>();

    // Конструктор для всего, кроме genres
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                Integer mpa, Integer rate) {

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

    // конструктор для всего вообще
    public Film(Integer id,
                String name, String description, LocalDate releaseDate, Integer duration, Integer mpa,
                Integer rate,
                ArrayList<LinkedHashMap<String, Integer>> genres) {

        this.id = id;
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

    /*
        Метод заполнения Genres
    */
    private ArrayList<Integer> addGenres(ArrayList<LinkedHashMap<String, Integer>> inputGenres) {

        ArrayList<Integer> resultList = new ArrayList<>();

        for (int i = 0; i < inputGenres.size(); i++) {

            Integer genreId = inputGenres.get(i).get("id");
            resultList.add(genreId);

        }
        return resultList;
    }

}
