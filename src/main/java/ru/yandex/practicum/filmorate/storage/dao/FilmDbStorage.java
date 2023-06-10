package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.KVClass;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Integer durationMin = rs.getInt("duration_min");
        Integer ratingId = rs.getInt("rating_id");
        Integer likes = rs.getInt("likes");

        // достаём список жанров фильма из таблиц film_genre и genre
        String sqlQuery = "SELECT " +
                "fg.genre_id, " +
                "g.genre_name " +
                "FROM film_genre AS fg " +
                "JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = " + filmId + " " +
                "ORDER BY fg.genre_id ASC";
        ArrayList<KVClass> genres =
                new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenreKV));

        return new Film(filmId, name, description, releaseDate, durationMin, getMpaByMpaId(ratingId), likes, genres);
    }

    @Override
    public Film addFilm(Film film) {

        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();
        Integer ratingId = film.getMpa().getId();
        Integer likes = film.getRate();
        ArrayList<KVClass> genres = film.getGenres();

        // добавляем фильм в таблицу films
        String sqlQuery = "INSERT INTO film(name, description, release_date, duration_min, rating_id, likes)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                name,
                description,
                releaseDate,
                duration,
                ratingId,
                likes);

        // Получаем id добавленного фильма в БД
        Integer filmId = jdbcTemplate.queryForObject("SELECT film_id FROM film WHERE " +
                        "name = '" + name + "' AND duration_min = '" + duration + "'",
                Integer.class);

        // добавляем фильм в таблицу film_genre
        for (KVClass genreMap : genres) {
            Integer genreId = genreMap.getId();
            jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (? , ?)", filmId, genreId);
        }

        // достаём созданный фильм из таблицы фиьмов
        sqlQuery = "SELECT * FROM film WHERE " +
                "name = ? " +
                "AND description = ? " +
                "AND release_date = ? " +
                "AND duration_min = ? ";

        Film filmToReturn = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm,
                name,
                description,
                releaseDate,
                duration);

        return filmToReturn;
    }

    @Override
    public Film updateFilm(Film film) {

        Integer filmId = film.getId();
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer durationMin = film.getDuration();
        Integer ratingId = film.getMpa().getId();
        Integer likes = film.getRate();
        ArrayList<KVClass> genres = film.getGenres();

        // обновляем фильм в таблице film
        String sqlQuery = "UPDATE film SET " +
                "name = ? " +
                ", description = ? " +
                ", release_date = ? " +
                ", duration_min = ? " +
                ", rating_id = ? " +
                ", likes = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery,
                name,
                description,
                releaseDate,
                durationMin,
                ratingId,
                likes,
                filmId);

        // обновляем фильм в таблице film_genre
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        for (KVClass genreMap : genres) {
            Integer genreId = genreMap.getId();
            jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (? , ?)", filmId, genreId);
        }

        // достаём обновлённый фильм из таблицы
        sqlQuery = "SELECT * FROM film WHERE film_id = ?";

        Film updatedFilm = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);

        return updatedFilm;
    }

    @Override
    public Collection<Film> getAllFilms() {

        String sqlQuery = "SELECT film_id, name, description, release_date, duration_min, rating_id, likes FROM film";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilm(Integer filmId) {

        String sqlQuery = "select * from film where film_id = ?";

        Film filmToReturn = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);

        return filmToReturn;

    }

    @Override
    public boolean containsKey(Integer filmId) {

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where film_id = ?", filmId);

        return filmRows.next();
    }

    @Override
    public void addLike(Integer filmId) {

        Film filmToUpdate = getFilm(filmId);
        Integer currentRate = filmToUpdate.getRate();
        filmToUpdate.setRate(currentRate + 1);
        updateFilm(filmToUpdate);
    }

    @Override
    public void removeLike(Integer filmId) {

        Film filmToUpdate = getFilm(filmId);
        Integer currentRate = filmToUpdate.getRate();
        filmToUpdate.setRate(currentRate - 1);
        updateFilm(filmToUpdate);
    }

    @Override
    public Integer getLikes(Integer filmId) {

        return jdbcTemplate.queryForObject("SELECT likes FROM film WHERE film_id = ?",
                Integer.class, filmId);
    }

    /*
        Методы для базы рейтингов
     */
    @Override
    public boolean containsMpaId(Integer mpaId) {

        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("select * from rating where rating_id = ?", mpaId);

        return mpaRow.next();
    }

    private KVClass mapRowToMpaKV(ResultSet rs, int rowNum) throws SQLException {

        Integer id = rs.getInt("rating_id");
        String name = rs.getString("rating_name");

        return new KVClass(id, name);
    }

    @Override
    public KVClass getMpaByMpaId(Integer mpaId) {

        String sqlQuery = "SELECT rating_id, rating_name FROM rating WHERE rating_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpaKV, mpaId);
    }

    @Override
    public Collection<KVClass> getAllMpa() {

        String sqlQuery = "SELECT rating_id, rating_name FROM rating";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpaKV);
    }

    /*
        Методы для базы жанров
     */
    @Override
    public boolean containsGenreId(Integer genreId) {

        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("select * from genre where genre_id = ?", genreId);

        return genreRow.next();
    }

    private KVClass mapRowToGenreKV(ResultSet rs, int rowNum) throws SQLException {

        Integer id = rs.getInt("genre_id");
        String name = rs.getString("genre_name");

        return new KVClass(id, name);
    }

    @Override
    public KVClass getGenre(Integer genreId) {

        String sqlQuery = "SELECT genre_id, genre_name FROM genre WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenreKV, genreId);
    }

    @Override
    public Collection<KVClass> getAllGenres() {

        String sqlQuery = "SELECT genre_id, genre_name FROM genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenreKV);
    }
}