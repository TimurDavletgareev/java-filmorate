package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    boolean containsKey(Integer filmId);

    Film getFilm(Integer filmId);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    Integer getRating(Integer filmId);

}
