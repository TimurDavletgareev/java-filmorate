package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    // объявили переменную сервиса фильмов
    private final FilmService filmService; // lombok @RequiredArgsConstructor создаёт конструктор

    /*
        Эндпоинт получения фильма по id
    */
    @GetMapping("{id}")
    public Film getFilmById(@PathVariable Integer id) {

        return filmService.getFilmById(id);
    }

    /*
        Эндпоинт получения списка всех фильмов
     */
    @GetMapping
    public List<Film> getAllFilms() {

        return new ArrayList<>(filmService.getAllFilms());
    }

    /*
        Эндпоинт добавления фильма
     */
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        return filmService.addFilm(film);
    }

    /*
        Эндпоинт обновления фильма
    */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        return filmService.updateFilm(film);
    }

    /*
        Эндпоинт добавления и удаления лайка от пользователя фильму
     */
    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {

        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {

        filmService.deleteLike(id, userId);
    }

    /*
        Эндпоинт получения списка популярных фильмов
     */
    @GetMapping("popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) final Integer count) {

        return new ArrayList<>(filmService.getPopular(count));
    }


}
