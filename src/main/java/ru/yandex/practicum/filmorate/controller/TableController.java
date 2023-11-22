package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.KVClass;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

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
    public KVClass getMpaByMpaId(@PathVariable Integer id) {

        return filmService.getMpaByMpaId(id);
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
    public KVClass getGenre(@PathVariable Integer id) {

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