package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.KVClass;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int idCounter = 1; // Переменная для создания уникальных id
    private final Map<Integer, Film> films = new HashMap<>(); // карта для хранения фильмов по ключу id
    private Map<Integer, List<Integer>> likes = new HashMap<>();

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {

        film.setId(idCounter);
        films.put(film.getId(), film);
        idCounter++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean containsKey(Integer filmId) {

        return films.containsKey(filmId);
    }

    @Override
    public Film getFilm(Integer filmId) {
        return films.get(filmId);
    }

    @Override
    public void addLike(Integer filmId) {


    }

    @Override
    public void removeLike(Integer filmId) {

    }

    @Override
    public Integer getLikes(Integer filmId) {
        return likes.get(filmId).size();
    }

    @Override
    public boolean containsMpaId(Integer mpaId) {
        return false;
    }

    @Override
    public KVClass getMpaByMpaId(Integer mpaId) {
        return null;
    }

    @Override
    public Collection<KVClass> getAllMpa() {
        return null;
    }

    @Override
    public boolean containsGenreId(Integer genreId) {
        return false;
    }

    @Override
    public String getGenre(Integer genreId) {
        return null;
    }

    @Override
    public Collection<KVClass> getAllGenres() {
        return null;
    }


}
