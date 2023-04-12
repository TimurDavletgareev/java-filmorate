package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int idCounter = 1; // Временная переменная для создания id


    @GetMapping
    public List<User> getAllUsers() {

        return new ArrayList<>(users.values());

    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            user.setId(idCounter);
            users.put(user.getId(), user);
            idCounter++;

        return users.getOrDefault(user.getId(), null);

    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            if (!users.containsKey(user.getId())) {
                log.error("Пользователя с указанным id нет в базе");
                throw new FrValidationException("Пользователя с указанным id нет в базе");
            }
            users.put(user.getId(), user);

        return users.getOrDefault(user.getId(), null);

    }

}
