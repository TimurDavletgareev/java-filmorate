package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    // объявили переменную хранилища фильмов с внедрённой зависимостью от интерфейса хранилища фильмов
    private final FilmStorage filmStorage;
    // объявили переменную хранилища пользователей с внедрённой зависимостью от интерфейса хранилища пользователей
    private final UserService userService;

    // lombok @RequiredArgsConstructor создаёт конструктор

    /*
        Метод проверки наличия id фильма в базе
    */
    public void isValidId(int id) {

        if (!filmStorage.containsKey(id)) {

            throw new NotFoundException("Фильма с указанным id нет в базе");
        }
    }

    /*
        Методы работы с хранилищем фильмов
     */
    public List<Film> getAllFilms() {

        return new ArrayList<>(filmStorage.getAllFilms());
    }

    public Film addFilm(Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmValidationException("Указана дата до 28.12.1895");
        }

        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {

        isValidId(film.getId());

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmValidationException("Указана дата до 28.12.1895");
        }

        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Integer id) {

        isValidId(id);
        return filmStorage.getFilm(id);
    }

    /*
        Методы добавления и удаления лайков с зависимостью от UserService
    */
    public void addLike(Integer filmId, Integer userId) {

        isValidId(filmId);
        userService.isValidId(userId);

        Film film = filmStorage.getFilm(filmId);

        filmStorage.getFilm(filmId).addUserToLikedList(userId);
        userService.addLikeFromUser(userId, film);
    }

    public void deleteLike(Integer filmId, Integer userId) {

        isValidId(filmId);
        userService.isValidId(userId);

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
