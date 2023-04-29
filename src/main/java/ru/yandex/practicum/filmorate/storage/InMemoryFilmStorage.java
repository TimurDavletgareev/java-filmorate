package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private int idCounter = 1; // Переменная для создания уникальных id
    private final Map<Integer, Film> films = new HashMap<>(); // карта для хранения фильмов по ключу id

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {

        film.setId(idCounter);
        films.put(film.getId(), film);
        idCounter++;
        return null;
    }

    @Override
    public Film updateFilm(Film film) {

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean containsKey(Integer id) {

        return films.containsKey(id);
    }
}
