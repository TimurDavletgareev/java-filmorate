package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getAllUsers();

    User addUser(User user);

    User updateUser(User user);

    boolean containsKey(Integer userId);

    User getUser(Integer userId);

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    Collection<Integer> getFriends(Integer userId);

    void addLikeToFilm(Integer userId, Integer filmId);

    void removeLikeFromFilm(Integer userId, Integer filmId);

}
