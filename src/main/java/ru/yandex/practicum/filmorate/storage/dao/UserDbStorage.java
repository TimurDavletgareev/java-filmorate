package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmStorage filmStorage;

    public UserDbStorage(JdbcTemplate jdbcTemplate, FilmStorage filmStorage) {

        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
    }


    /*
        Метод маппинга строк таблицы "users" в объекты User
     */
    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {

        Integer userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");

        // Получаем дату и конвертируем её из sql.Date в time.LocalDate
        LocalDate birthday = rs.getDate("birthday_date").toLocalDate();

        return new User(userId, login, name, email, birthday);
    }

    @Override
    public Collection<User> getAllUsers() {

        String sqlQuery = "SELECT user_id, email, login, name, birthday_date FROM users";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {

        String email = user.getEmail();
        String login = user.getLogin();
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        String name = user.getName();
        LocalDate birthday = user.getBirthday();

        // добавляем пользователя в таблицу
        String sqlQuery = "INSERT INTO users(login, name, email, birthday_date) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery,
                login,
                name,
                email,
                birthday);

        // достаём созданного пользователя из таблицы
        sqlQuery = "SELECT * FROM users WHERE " +
                "login = ? " +
                "AND name = ? " +
                "AND email = ? " +
                "AND birthday_date = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, login, name, email, birthday);
    }

    @Override
    public User updateUser(User user) {

        String email = user.getEmail();
        String login = user.getLogin();
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        String name = user.getName();
        LocalDate birthday = user.getBirthday();

        // обновляем пользователя
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ? , birthday_date = ?" +
                "WHERE user_id = ?";

        jdbcTemplate.update(sqlQuery,
                email,
                login,
                name,
                birthday,
                user.getId());

        // достаём обновлённого пользователя из таблицы
        sqlQuery = "SELECT * FROM users WHERE user_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, user.getId());
    }

    @Override
    public boolean containsKey(Integer userId) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", userId);

        return userRows.next();
    }

    @Override
    public User getUser(Integer userId) {

        String sqlQuery = "select * from users where user_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    }

    /*
        Friends methods
     */
    @Override
    public void addFriend(Integer userId, Integer friendId) {

        String sqlQueryForCheck = "SELECT * FROM user_friends WHERE user_id = ? AND friend_id = ?";
        String sqlQueryForStatus = "SELECT friend_status_id FROM user_friends WHERE user_id = ? AND friend_id = ?";

        SqlRowSet friendsRow = jdbcTemplate.queryForRowSet(sqlQueryForCheck, userId, friendId);

        Integer currentFriendStatus;

        if (friendsRow.next()) {

            currentFriendStatus = jdbcTemplate.queryForObject(sqlQueryForStatus, Integer.class, userId, friendId);

            if (currentFriendStatus == 1) {

                jdbcTemplate.update("UPDATE user_friends SET friend_status_id = ? WHERE " +
                                "user_id = ? AND friend_id = ?",
                        FriendStatus.FRIENDS, userId, friendId);

                jdbcTemplate.update("UPDATE user_friends SET friend_status_id = ? WHERE " +
                                "user_id = ? AND friend_id = ?",
                        FriendStatus.FRIENDS, friendId, userId);
            }

        } else {

            jdbcTemplate.update("INSERT INTO user_friends (user_id, friend_id, friend_status_id) " +
                    "VALUES (?, ?, ?)", userId, friendId, FriendStatus.FOLLOWS);

            jdbcTemplate.update("INSERT INTO user_friends (user_id, friend_id,friend_status_id) " +
                    "VALUES (?, ?, ?)", friendId, userId, FriendStatus.FOLLOWED_BY);
        }

    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {

        String sqlQueryForCheck = "SELECT * FROM user_friends WHERE user_id = ? AND friend_id = ?";
        String sqlQueryForStatus = "SELECT friend_status_id FROM user_friends WHERE user_id = ? AND friend_id = ?";

        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(sqlQueryForCheck, userId, friendId);

        Integer currentFriendStatus;

        if (friendsRows.next()) {

            currentFriendStatus = jdbcTemplate.queryForObject(sqlQueryForStatus, Integer.class, userId, friendId);

            if (currentFriendStatus == 0) {

                jdbcTemplate.update("DELETE FROM user_friends WHERE " +
                        "user_id = ? AND friend_id = ?", userId, friendId);

                jdbcTemplate.update("DELETE FROM user_friends WHERE " +
                        "user_id = ? AND friend_id = ?", friendId, userId);

            } else if (currentFriendStatus == 2) {

                jdbcTemplate.update("INSERT INTO user_friends (user_id, friend_id, friend_status_id) " +
                        "VALUES (?, ?, ?)", userId, friendId, FriendStatus.FOLLOWED_BY);

                jdbcTemplate.update("INSERT INTO user_friends (user_id, friend_id,friend_status_id) " +
                        "VALUES (?, ?, ?)", friendId, userId, FriendStatus.FOLLOWS);

            }
        }
    }

    private Integer getIdFromFriend(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }

    @Override
    public Collection<Integer> getFriends(Integer userId) {

        String sql = "SELECT * FROM user_friends WHERE user_id = ? AND friend_status_id = 0 OR friend_status_id = 2";

        return jdbcTemplate.query(sql, (rs, rowNum) -> getIdFromFriend(rs), userId);
    }

    /*
        Likes methods
     */
    @Override
    public void addLikeToFilm(Integer userId, Integer filmId) {

        String sqlQueryForCheck = "SELECT * FROM film_likes WHERE film_id = ? AND user_id = ?";

        SqlRowSet likeRow = jdbcTemplate.queryForRowSet(sqlQueryForCheck, filmId, userId);

        if (!likeRow.next()) {
            jdbcTemplate.update("INSERT INTO film_likes (film_id, user_id) " +
                    "VALUES (?, ?)", filmId, userId);
            filmStorage.addLike(filmId);
        }

    }

    @Override
    public void removeLikeFromFilm(Integer userId, Integer filmId) {

        String sqlQueryForCheck = "SELECT * FROM film_likes WHERE film_id = ? AND user_id = ?";

        SqlRowSet likeRow = jdbcTemplate.queryForRowSet(sqlQueryForCheck, filmId, userId);

        if (likeRow.next()) {
            jdbcTemplate.update("DELETE FROM film_likes " +
                    "WHERE film_id = ? AND user_id = ?", filmId, userId);
            filmStorage.removeLike(filmId);
        }
    }
}
