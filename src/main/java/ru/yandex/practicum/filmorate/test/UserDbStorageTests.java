package ru.yandex.practicum.filmorate.test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTests {
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
    User user1;
    User user2;
    User user3;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    void beforeEach() {

        user1 = new User("email1@e.m", "login1", LocalDate.parse("2001-01-01", formatter));
        user2 = new User("email2@e.m", "login2", LocalDate.parse("2002-02-02", formatter));
        user3 = new User("email3@e.m", "login3", LocalDate.parse("2003-03-03", formatter));

    }

    @AfterEach
    void afterEach() {

        String sql;
        sql = "DELETE from user_friends";
        jdbcTemplate.update(sql);
        sql = "DELETE from users";
        jdbcTemplate.update(sql);
        sql = "DELETE from film";
        jdbcTemplate.update(sql);

    }

    @Test
    public void testAddUser() {

        LocalDate birthday = LocalDate.parse("1985-03-25", formatter);
        User userToAdd = new User("testUser@email.com", "testUserLogin", birthday);

        User userToCheck = userStorage.addUser(userToAdd);

        Integer size = jdbcTemplate.queryForObject("SELECT COUNT (*) from users ", Integer.class);
        assertEquals(1, size);

        assertEquals("testUser@email.com", userToCheck.getEmail());

    }

    @Test
    public void testGetUserById() {

        userStorage.addUser(user1);

        Integer size = jdbcTemplate.queryForObject("SELECT COUNT (*) from users ", Integer.class);

        Integer id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login1'",
                Integer.class);

        User testUser = userStorage.getUser(id);

        assertEquals(testUser.getEmail(), user1.getEmail());
    }

    @Test
    public void testGetAllUsers() {

        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);
        ArrayList<User> allUsers = new ArrayList<>(userStorage.getAllUsers());

        for (User user : allUsers) {
            System.out.println(user);
        }

        Integer size = jdbcTemplate.queryForObject("SELECT COUNT (*) from users ", Integer.class);
        assertEquals(3, size);
    }

    @Test
    public void testUpdateUser() {

        LocalDate birthday = LocalDate.parse("1985-03-25", formatter);
        User userToAdd = new User("testUser@email.com", "testUserLogin", birthday);
        userStorage.addUser(userToAdd);

        Integer id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'testUserLogin'",
                Integer.class);

        System.out.println("id = " + id);

        User updatedUser = new User(id, "updated@mail.com", "updLogin", "updName", birthday);
        userStorage.updateUser(updatedUser);

        User userToCheck = userStorage.getUser(id);

        assertEquals(userToCheck, updatedUser);
    }

    @Test
    public void testContainsKey() {

        userStorage.addUser(user1);
        Integer id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login1'",
                Integer.class);
        assertTrue(userStorage.containsKey(id));
        assertFalse(userStorage.containsKey(1000));
    }

    @Test
    public void testAddToFriends() {

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        Integer user1id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login1'",
                Integer.class);
        Integer user2id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login2'",
                Integer.class);

        userStorage.addFriend(user1id, user2id);

        SqlRowSet friendsRaw1 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 1);
        SqlRowSet friendsRaw2 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 2);

        if (friendsRaw1.next()) {
            Integer status1 = friendsRaw1.getInt("friend_status_id");
            System.out.println(status1);
            assertEquals(0, status1);
        }
        if (friendsRaw2.next()) {
            Integer status2 = friendsRaw2.getInt("friend_status_id");
            System.out.println(status2);
            assertEquals(1, status2);
        }

        userStorage.addFriend(user2id, user1id);

        friendsRaw1 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 1);
        friendsRaw2 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 2);

        if (friendsRaw1.next()) {
            Integer status1 = friendsRaw1.getInt("friend_status_id");
            System.out.println(status1);
            assertEquals(2, status1);
        }
        if (friendsRaw2.next()) {
            Integer status2 = friendsRaw2.getInt("friend_status_id");
            System.out.println(status2);
            assertEquals(2, status2);
        }
    }

    @Test
    public void testDeleteFromFriends() {

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        Integer user1id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login1'",
                Integer.class);
        Integer user2id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login2'",
                Integer.class);

        userStorage.addFriend(user1id, user2id);

        SqlRowSet friendsRaw1 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 1);
        SqlRowSet friendsRaw2 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 2);

        if (friendsRaw1.next()) {
            Integer status1 = friendsRaw1.getInt("friend_status_id");
            System.out.println(status1);
            assertEquals(0, status1);
        }
        if (friendsRaw2.next()) {
            Integer status2 = friendsRaw2.getInt("friend_status_id");
            System.out.println(status2);
            assertEquals(1, status2);
        }

        userStorage.removeFriend(user1id, user2id);

        friendsRaw1 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 1);
        friendsRaw2 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 2);

        assertFalse(friendsRaw1.next());
        assertFalse(friendsRaw2.next());

        userStorage.addFriend(user1id, user2id);
        userStorage.addFriend(user2id, user1id);

        friendsRaw1 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 1);
        friendsRaw2 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 2);

        if (friendsRaw1.next()) {
            Integer status1 = friendsRaw1.getInt("friend_status_id");
            System.out.println(status1);
            assertEquals(2, status1);
        }
        if (friendsRaw2.next()) {
            Integer status2 = friendsRaw2.getInt("friend_status_id");
            System.out.println(status2);
            assertEquals(2, status2);
        }

        userStorage.removeFriend(user1id, user2id);

        friendsRaw1 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 1);
        friendsRaw2 = jdbcTemplate.queryForRowSet("select * from user_friends where " +
                "is_friend_id = ?", 2);

        if (friendsRaw1.next()) {
            Integer status1 = friendsRaw1.getInt("friend_status_id");
            System.out.println(status1);
            assertEquals(1, status1);
        }
        if (friendsRaw2.next()) {
            Integer status2 = friendsRaw2.getInt("friend_status_id");
            System.out.println(status2);
            assertEquals(0, status2);
        }



    }

    @Test
    public void testGetUserFriends() {

        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);

        Integer user1id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login1'",
                Integer.class);
        Integer user2id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login2'",
                Integer.class);
        Integer user3id = jdbcTemplate.queryForObject("SELECT user_id from users WHERE login = 'login3'",
                Integer.class);

        userStorage.addFriend(user1id, user2id);
        userStorage.addFriend(user1id, user3id);

        ArrayList<Integer> friendsListOfUser1 = new ArrayList<>(userStorage.getFriends(user1id));

        assertEquals(2, friendsListOfUser1.size());
        System.out.println("size of user1 friends list: " + friendsListOfUser1.size());

        assertEquals(user2id, friendsListOfUser1.get(0));
        assertEquals(user3id, friendsListOfUser1.get(1));

    }

}