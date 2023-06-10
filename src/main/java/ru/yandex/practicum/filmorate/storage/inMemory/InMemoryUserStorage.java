package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int idCounter = 1; // Переменная для создания уникальных id
    private final Map<Integer, User> users = new HashMap<>();
    private Map<Integer, List<Integer>> friends = new HashMap<>();
    private Map<Integer, List<Integer>> filmsLiked = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {

        user.setId(idCounter);
        if (user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        idCounter++;

        return users.getOrDefault(user.getId(), null);
    }

    @Override
    public User updateUser(User user) {

        users.put(user.getId(), user);

        return users.getOrDefault(user.getId(), null);
    }

    @Override
    public boolean containsKey(Integer userId) {
        return users.containsKey(userId);
    }

    @Override
    public User getUser(Integer userId) {
        return users.get(userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        friends.get(userId).add(friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        friends.get(userId).remove(friendId);
    }

    @Override
    public Collection<Integer> getFriends(Integer userId) {
        return friends.get(userId);
    }

    @Override
    public void addLikeToFilm(Integer userId, Integer filmId) {

        filmsLiked.get(userId).add(filmId);
    }

    @Override
    public void removeLikeFromFilm(Integer userId, Integer filmId) {

        filmsLiked.get(userId).remove(filmId);
    }
}
