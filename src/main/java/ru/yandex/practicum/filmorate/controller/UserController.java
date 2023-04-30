package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    /*
        Метод проверки наличий id пользователя в базе
     */
    public void isValidId(int id) {

/*
        if (id <= 0) {
            throw new BadRequestException("id должен быть больше нуля");
        }
*/

        if (!userService.containsUserId(id)) {
            log.error("Пользователя с указанным id нет в базе");
            throw new NotFoundException("Пользователя с указанным id нет в базе");
        }
    }

    /*
        Эндпоинт получения пользователя по id
    */
    @GetMapping("{id}")
    public User getUserById(@PathVariable Integer id) {

        isValidId(id);
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

        isValidId(user.getId());

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

        isValidId(id);
        isValidId(friendId);
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {

        isValidId(id);
        isValidId(friendId);
        userService.deleteFromFriends(id, friendId);
    }

    /*
        Эндпоинт получения списка друзей пользователя
    */
    @GetMapping("{id}/friends")
    public List<User> getFriendsList(@PathVariable Integer id) {

        isValidId(id);

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

        isValidId(id);
        isValidId(otherId);

        List<User> result = new ArrayList<>();

        for (Integer eachId : userService.getCommonFriends(id, otherId)) {
            result.add(userService.getUserById(eachId));
        }

        return result;

    }

}
