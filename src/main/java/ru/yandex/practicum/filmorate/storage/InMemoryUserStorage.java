package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{

    private int idCounter = 1; // Переменная для создания уникальных id
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {

        user.setId(idCounter);
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
    public boolean containsKey(Integer id) {
        return users.containsKey(id);
    }
}
