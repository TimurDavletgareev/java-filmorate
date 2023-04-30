package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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

    FilmService filmService; // объявили переменную хранилища фильмов

    // конструктор с внедрённой зависимостью от интерфейса хранилища фильмов
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /*
        Метод проверки наличия id фильма в базе
    */
    public void isValidId(int id) {

        if (!filmService.containsFilmId(id)) {
            log.error("Фильма с указанным id нет в базе");
            throw new NotFoundException("Фильма с указанным id нет в базе");
        }
    }

    /*
        Метод проверки наличия id пользователя в базе
    */
    public void isValidUserId(int id) {

        if (!filmService.containsUserId(id)) {
            log.error("Пользователя с указанным id нет в базе");
            throw new NotFoundException("Пользователя с указанным id нет в базе");
        }
    }

    /*
        Эндпоинт получения фильма по id
    */
    @GetMapping("{id}")
    public Film getFilmById(@PathVariable Integer id) {

        isValidId(id);
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

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {

            log.error("Указана дата до 28.12.1895");

            throw new FilmValidationException("Указана дата до 28.12.1895");
        }

        return filmService.addFilm(film);
    }

    /*
        Эндпоинт обновления фильма
    */
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        isValidId(film.getId());

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Указана дата до 28.12.1895");
            throw new FilmValidationException("Указана дата до 28.12.1895");
        }

        return filmService.updateFilm(film);
    }



    /*
        Эндпоинт добавления и удаления лайка от пользователя фильму
     */
    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {

        isValidId(id);
        isValidUserId(userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {

        isValidId(id);
        isValidUserId(userId);
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
