package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    // @Qualifier("UserDbStorage") // указали, что будет использована имплементация UserStorage UserDbStorage
    private final UserStorage userStorage; // переменная хранилища пользователей

    /*
        Метод проверки наличия id пользователя в базе
    */
    public void isValidId(int id) {

        if (!userStorage.containsKey(id)) {

            throw new NotFoundException("Пользователя с указанным id нет в базе");
        }
    }

    /*
        Методы работы с хранилищем пользователей
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.getAllUsers());
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {

        isValidId(user.getId());
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer id) {

        isValidId(id);
        return userStorage.getUser(id);
    }

    /*
        Методы добавления и удаления друзей
     */
    public void addToFriends(Integer id1, Integer id2) {

        isValidId(id1);
        isValidId(id2);

        userStorage.addFriend(id1, id2);
        userStorage.addFriend(id2, id1);
    }

    public void deleteFromFriends(Integer id1, Integer id2) {

        isValidId(id1);
        isValidId(id2);

        userStorage.removeFriend(id1, id2);
        userStorage.removeFriend(id2, id1);
    }

    /*
        Метод получения списка друзей дользователя
     */
    public Collection<Integer> getFriendsList(Integer user_id) {

        isValidId(user_id);
        return userStorage.getFriends(user_id);
    }

    /*
        Метод получения списка общих друзей двух пользователей
     */
    public Collection<Integer> getCommonFriends(Integer id1, Integer id2) {

        isValidId(id1);
        isValidId(id2);

        List<Integer> result = new ArrayList<>();
        for (Integer id : userStorage.getFriends(id1)) {
            if (userStorage.getFriends(id2).contains(id)) {
                result.add(id);
            }
        }
        return result;
    }

    /*
        Методы добавления и удаления лайка (вызываются из FilmService, там же проверяются на соответствия базам)
     */
    protected void addLikeFromUser(Integer userId, Integer filmId) {
        userStorage.addLikeToFilm(userId, filmId);
    }

    protected void removeLikeFromUser(Integer userId, Integer filmId) {
        userStorage.removeLikeFromFilm(userId, filmId);
    }
}
