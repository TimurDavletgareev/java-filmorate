package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    UserStorage userStorage; // переменная хранилища пользователей

    /*
        В конструктор класса внедрена зависимость от интерфейса хранилища пользователей
     */
    public UserService(UserStorage userStorage) {

        this.userStorage = userStorage;
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
        return userStorage.updateUser(user);
    }

    public boolean containsUserId(Integer id) {
        return userStorage.containsKey(id);
    }

    public User getUserById(Integer id) { return userStorage.getUser(id); }

    /*
        Методы добавления и удаления друзей
     */
    public void addToFriends(Integer id1, Integer id2) {

        userStorage.getUser(id1).addFriend(id2);
        userStorage.getUser(id2).addFriend(id1);
    }

    public void deleteFromFriends(Integer id1, Integer id2) {

        userStorage.getUser(id1).removeFriend(id2);
        userStorage.getUser(id2).removeFriend(id1);
    }

    /*
        Метод получения списка друзей дользователя
     */
    public Collection<Integer> getFriendsList(Integer id){
        return userStorage.getUser(id).getFriends();
    }

    /*
        Метод получения списка общих друзей двух пользователей
     */
    public Collection<Integer> getCommonFriends(Integer id1, Integer id2) {

        User user1 = userStorage.getUser(id1);
        User user2 = userStorage.getUser(id2);

        List<Integer> result = new ArrayList<>();
        for (Integer id : user1.getFriends()) {
            if (user2.getFriends().contains(id)) {
                result.add(id);
            }
        }
        return result;
    }

    /*
        Методы добавления и удаления лайка (вызываются из FilmService)
     */
    protected void addLikeFromUser(Integer userId, Film film) {

        userStorage.getUser(userId).addLikeToFilm(film);
    }

    protected void removeLikeFromUser(Integer userId, Film film) {
        userStorage.getUser(userId).removeLikeFromFilm(film);
    }
}
