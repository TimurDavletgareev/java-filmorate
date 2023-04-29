package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    // private final Map<Integer, User> users = new HashMap<>();
    // private int idCounter = 1; // Временная переменная для создания id

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {

        // return new ArrayList<>(users.values());
        return new ArrayList<>(userService.getAllUsers());

    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        //user.setId(idCounter);
        //users.put(user.getId(), user);
        //idCounter++;

        // return users.getOrDefault(user.getId(), null);
        return userService.addUser(user);

    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (!userService.containsUser(user)) {
            log.error("Пользователя с указанным id нет в базе");
            throw new FrValidationException("Пользователя с указанным id нет в базе");
        }

        // users.put(user.getId(), user);
        // return users.getOrDefault(user.getId(), null);
        return userService.updateUser(user);

    }

}
