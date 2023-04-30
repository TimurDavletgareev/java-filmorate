package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private int id;
    private Set<Integer> friends = new HashSet<>();
    private Set<Film> filmsLiked = new HashSet<>();

    @NotNull
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private String name;
    @Past
    private final LocalDate birthday;

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void removeFriend(Integer id) {

        friends.remove(id);
    }

    public void addLikeToFilm(Film film) {

        filmsLiked.add(film);
    }

    public void removeLikeFromFilm(Film film) {

        filmsLiked.remove(film);
    }

}
