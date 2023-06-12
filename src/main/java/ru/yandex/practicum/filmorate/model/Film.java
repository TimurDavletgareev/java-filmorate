package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private KVClass mpa;
    private Integer rate;
    private HashSet<KVClass> genres = new HashSet<>();


    // конструктор для всего вообще
    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                KVClass mpa, Integer rate, HashSet<KVClass> genres) {

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

}
