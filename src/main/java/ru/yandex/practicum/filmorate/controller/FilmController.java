package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int idCounter = 1; // Временная переменная для создания id

    @GetMapping
    public List<Film> getAllFilms() {

        return new ArrayList<>(films.values());

    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {

            log.error("Указана дата до 28.12.1895");

            throw new frValidationException("Указана дата до 28.12.1895");
        }
        film.setId(idCounter);
        films.put(film.getId(), film);
        idCounter++;

        return films.getOrDefault(film.getId(), null);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указана дата до 28.12.1895");
            throw new frValidationException("Указана дата до 28.12.1895");
        }
        if (!films.containsKey(film.getId())) {
            log.error("Фильма с указанным id нет в базе");
            throw new frValidationException("Фильма с указанным id нет в базе");
        }
        films.put(film.getId(), film);

        return films.getOrDefault(film.getId(), null);

    }

}
