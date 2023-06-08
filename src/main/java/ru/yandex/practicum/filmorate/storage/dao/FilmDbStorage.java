package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /*
        Метод маппинга строк таблицы "film" в объекты Film
     */
    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {

        Integer filmId = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        Integer durationMin  = rs.getInt("duration_min");
        Integer ratingId  = rs.getInt("rating_id");

        return new Film(filmId, name, description, releaseDate, durationMin, ratingId);
    }

    @Override
    public Collection<Film> getAllFilms() {

        String sqlQuery = "SELECT film_id, name, description, release_date, duration_min, rating_id FROM film";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {

        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();
        Integer mpa = film.getMpa();

        // добавляем фильм в таблицу
        String sqlQuery = "INSERT INTO film(name, description, release_date, duration_min, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                name,
                description,
                releaseDate,
                duration,
                mpa);

        // достаём созданный фильм из таблицы
        sqlQuery = "SELECT * FROM film WHERE " +
                "name = ? " +
                "AND description = ? " +
                "AND release_date = ? " +
                "AND duration_min = ? " +
                "AND rating_id = ? ";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm,
                name,
                description,
                releaseDate,
                duration,
                mpa);
    }

    @Override
    public Film updateFilm(Film film) {

        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer durationMin = film.getDuration();
        Integer ratingId = film.getMpa();

        // обновляем фильм
        String sqlQuery = "UPDATE film SET " +
                "name = ? " +
                ", description = ? " +
                ", release_date = ? " +
                ", duration_min = ? " +
                ", rating_id = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery,
                name,
                description,
                releaseDate,
                durationMin,
                ratingId,
                film.getFilmId());

        // достаём обновлённый фильм из таблицы
        sqlQuery = "SELECT * FROM film WHERE film_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, film.getFilmId());
    }

    @Override
    public boolean containsKey(Integer filmId) {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where film_id = ?", filmId);

        return filmRows.next();
    }

    @Override
    public Film getFilm(Integer filmId) {

        String sqlQuery = "select * from film where film_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);

    }

    @Override
    public void addLike(Integer filmId, Integer userId) {



    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {

    }

    @Override
    public Integer getRating(Integer filmId) {

        return jdbcTemplate.queryForObject("SELECT rating_id from film WHERE film_id = ?",
                Integer.class, filmId);
    }
}