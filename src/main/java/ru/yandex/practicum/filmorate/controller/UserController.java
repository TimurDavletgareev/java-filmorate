package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {

    // объявили переменную сервиса пользователей
    private final UserService userService; // lombok @RequiredArgsConstructor создаёт конструктор

    /*
        Эндпоинт получения пользователя по id
    */
    @GetMapping("{id}")
    public User getUserById(@PathVariable Integer id) {

        return userService.getUserById(id);
    }

    /*
        Эндпоинт получения списка всех пользователей
    */
    @GetMapping
    public List<User> getAllUsers() {

        return new ArrayList<>(userService.getAllUsers());
    }

    /*
        Эндпоинт добавления пользователя
     */
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {

        return userService.addUser(user);
    }

    /*
        Эндпоинт обновления пользователя
     */
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        return userService.updateUser(user);
    }

    /*
        Эндпоинты добавления и удаления друзей
     */
    @PutMapping("{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {

        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {

        userService.deleteFromFriends(id, friendId);
    }

    /*
        Эндпоинт получения списка друзей пользователя
    */
    @GetMapping("{id}/friends")
    public List<User> getFriendsList(@PathVariable Integer id) {

        List<User> result = new ArrayList<>();

        for (Integer eachId : userService.getFriendsList(id)) {
            result.add(userService.getUserById(eachId));
        }

        return result;
    }

    /*
        Эндпоинт получения списка взаимных друзей пользователей
    */
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {

        List<User> result = new ArrayList<>();

        for (Integer eachId : userService.getCommonFriends(id, otherId)) {
            result.add(userService.getUserById(eachId));
        }

        return result;

    }

}
