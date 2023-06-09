package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.KVClass;

import java.util.Collection;
import java.util.HashMap;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    boolean containsKey(Integer filmId);

    Film getFilm(Integer filmId);

    void addLike(Integer filmId);

    void removeLike(Integer filmId);

    Integer getLikes(Integer filmId);

    boolean containsMpaId(Integer mpaId);
    String getMpa(Integer mpaId);
    Collection<KVClass> getAllMpa();

    boolean containsGenreId(Integer genreId);
    String getGenre(Integer genreId);
    Collection<KVClass> getAllGenres();


}
