package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    // private final Map<Integer, Film> films = new HashMap<>();
    // private int idCounter = 1; // Временная переменная для создания id

    FilmService filmService; // объявили переменную хранилища фильмов

    // конструктор с внедрённой зависимостью от интерфейса хранилища фильмов
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmService.getAllFilms());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {

            log.error("Указана дата до 28.12.1895");

            throw new FrValidationException("Указана дата до 28.12.1895");
        }

        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указана дата до 28.12.1895");
            throw new FrValidationException("Указана дата до 28.12.1895");
        }
        if (!filmService.containsFilm(film)) {
            log.error("Фильма с указанным id нет в базе");
            throw new FrValidationException("Фильма с указанным id нет в базе");
        }

        return filmService.updateFilm(film);
    }

}
