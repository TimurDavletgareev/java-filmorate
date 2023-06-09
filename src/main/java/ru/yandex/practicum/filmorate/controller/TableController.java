package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.KVClass;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class TableController {

    // объявили переменную сервиса фильмов
    private final FilmService filmService; // lombok @RequiredArgsConstructor создаёт конструктор

    /*
    Эндпоинт получения MPA по id
 */
    @GetMapping("/mpa/{id}")
    public String getMpa(@PathVariable Integer id) {

        return filmService.getMpa(id);
    }

    /*
        Эндпоинт получения всех MPA
    */
    @GetMapping("/mpa")
    public Collection<KVClass> getAllMpa() {
        return filmService.getAllMpa();
    }

    /*
        Эндпоинт получения жанра по id
     */
    @GetMapping("/genres/{id}")
    public String getGenre(@PathVariable Integer id) {

        return filmService.getGenre(id);
    }

    /*
        Эндпоинт получения всех жанров
    */
    @GetMapping("/genres")
    public Collection<KVClass> getAllGenres() {
        return filmService.getAllGenres();
    }

}