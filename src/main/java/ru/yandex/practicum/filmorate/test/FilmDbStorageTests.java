package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.KVClass;
import ru.yandex.practicum.filmorate.model.MPAAList;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTests {
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;
    Film film1;
    Film film2;
    Film film3;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void beforeEach() {

        film1 = new Film("title1", "descr1", LocalDate.parse("2001-01-01", formatter),
                101, MPAAList.G);
        film2 = new Film("title2", "descr2", LocalDate.parse("2002-02-02", formatter),
                102, MPAAList.PG);
        film3 = new Film("title3", "descr3", LocalDate.parse("2003-03-03", formatter),
                103, MPAAList.PG13);
    }

    @AfterEach
    void afterEach() {

        String sql;
        sql = "DELETE from user_friends";
        jdbcTemplate.update(sql);

        sql = "DELETE from film_genre";
        jdbcTemplate.update(sql);

        sql = "DELETE from users";
        jdbcTemplate.update(sql);

        sql = "DELETE from film";
        jdbcTemplate.update(sql);
    }

    @Test
    void TestAddFilm() {

        LocalDate releaseDate = LocalDate.parse("1985-03-25", formatter);
        Film filmToAdd = new Film("testTitle", "testDescr", releaseDate, 120, MPAAList.G);

        Film filmToCheck = filmStorage.addFilm(filmToAdd);

        Integer size = jdbcTemplate.queryForObject("SELECT COUNT (*) from film ", Integer.class);
        assertEquals(1, size);

        assertEquals("testTitle", filmToCheck.getName());

    }

    @Test
    public void testGetFilmById() {

        filmStorage.addFilm(film1);

        Integer size = jdbcTemplate.queryForObject("SELECT COUNT (*) from film ", Integer.class);

        System.out.println(size);

        Integer id = jdbcTemplate.queryForObject("SELECT film_id from film WHERE name = 'title1'",
                Integer.class);

        System.out.println(id);

        Film testFilm = filmStorage.getFilm(id);

        assertEquals(testFilm.getName(), film1.getName());
    }

    @Test
    public void testGetAllFilms() {

        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        filmStorage.addFilm(film3);
        ArrayList<Film> allFilms = new ArrayList<>(filmStorage.getAllFilms());

        for (Film film : allFilms) {
            System.out.println(film);
        }

        Integer size = jdbcTemplate.queryForObject("SELECT COUNT (*) from film ", Integer.class);
        assertEquals(3, size);
    }

    @Test
    public void testUpdateFilm() {

        LocalDate releaseDate = LocalDate.parse("1985-03-25", formatter);
        Film filmToAdd = new Film("testTitle", "testDescr", releaseDate, 120, MPAAList.R);

        filmStorage.addFilm(filmToAdd);

        Integer id = jdbcTemplate.queryForObject("SELECT film_id from film WHERE name = 'testTitle'",
                Integer.class);

        System.out.println("id = " + id);

        Integer duration = 240;

        Film updatedFilm = new Film(id, "updatedTitle", "updDescr", releaseDate, duration,
                MPAAList.NC17);

        filmStorage.updateFilm(updatedFilm);

        Film filmToCheck = filmStorage.getFilm(id);

        assertEquals(filmToCheck, updatedFilm);
    }

    @Test
    public void testContainsKey() {

        filmStorage.addFilm(film1);
        Integer id = jdbcTemplate.queryForObject("SELECT film_id from film WHERE name = 'title1'",
                Integer.class);
        assertTrue(filmStorage.containsKey(id));
        assertFalse(filmStorage.containsKey(1000));
    }

    @Test
    public void testGetLikes() {

        LocalDate releaseDate = LocalDate.parse("1985-03-25", formatter);
        Integer rate = 6;
        HashSet<Integer> genres = new HashSet<>();
        Film filmToAdd = new Film("filmWithLikes", "descr", releaseDate, 120, MPAAList.G,
                rate, genres);
        filmStorage.addFilm(filmToAdd);

        Integer id = jdbcTemplate.queryForObject("SELECT film_id FROM film WHERE name = 'filmWithLikes'",
                Integer.class);

        System.out.println("id = " + id);
        System.out.println(filmStorage.getFilm(id));

        assertEquals(rate, filmStorage.getLikes(id));
    }

    @Test
    public void testGetMpa() {

        assertTrue(filmStorage.containsMpaId(1));
        assertFalse(filmStorage.containsMpaId(1000));
        assertEquals("G", filmStorage.getMpa(1));
        ArrayList<KVClass> list = new ArrayList<>(filmStorage.getAllMpa());
        System.out.println(list);
        assertEquals(list.get(0).getId(), 1);
        assertEquals(list.get(0).getName(), "G");


    }

    @Test
    public void testGetGenre() {

        assertTrue(filmStorage.containsGenreId(1));
        assertFalse(filmStorage.containsGenreId(1000));
        assertEquals("Комедия", filmStorage.getGenre(1));
        ArrayList<KVClass> list = new ArrayList<>(filmStorage.getAllGenres());
        System.out.println(list);
        assertEquals(list.get(0).getId(), 1);
        assertEquals(list.get(0).getName(), "Комедия");

    }
}