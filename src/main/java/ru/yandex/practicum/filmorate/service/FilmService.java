package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    FilmStorage filmStorage;
    UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    /*
        Методы работы с хранилищем фильмов
     */
    public List<Film> getAllFilms() {

        return new ArrayList<>(filmStorage.getAllFilms());
    }

    public Film addFilm(Film film) {

        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {

        return filmStorage.updateFilm(film);
    }

    public boolean containsFilmId(Integer id) {

        return filmStorage.containsKey(id);
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilm(id);
    }

    /*
        Метод проверки наличия пользователя через userService
     */
    public boolean containsUserId(Integer id) {

        return userService.containsUserId(id);
    }

    /*
        Методы добавления и удаления лайков с зависимостью от UserService
    */
    public void addLike(Integer filmId, Integer userId) {

        Film film = filmStorage.getFilm(filmId);

        filmStorage.getFilm(filmId).addUserToLikedList(userId);
        userService.addLikeFromUser(userId, film);
    }

    public void deleteLike(Integer filmId, Integer userId) {

        Film film = filmStorage.getFilm(filmId);

        filmStorage.getFilm(filmId).removeUserFromLikedList(userId);
        userService.removeLikeFromUser(userId, film);
    }

    /*
        Метод получения списка самых популярных фильмов
     */
    public Collection<Film> getPopular(int size) {

        return getAllFilms().stream()
                .sorted((f0, f1) -> -1 * (f0.getRating() - f1.getRating())) // -1 т.к. сортируем по убыванию
                .limit(size)
                .collect(Collectors.toList());
    }


}
