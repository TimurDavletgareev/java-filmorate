package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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

        film.setFilmId(idCounter);
        films.put(film.getFilmId(), film);
        idCounter++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {

        films.put(film.getFilmId(), film);
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
    public void addLike(Integer filmId, Integer userId) {

        likes.get(filmId).add(userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        likes.get(filmId).remove(userId);;
    }

    @Override
    public Integer getRating(Integer filmId) {
        return likes.get(filmId).size();
    }

}
