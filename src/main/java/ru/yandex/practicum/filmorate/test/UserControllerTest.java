package ru.yandex.practicum.filmorate.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    UserController userController;
    User user;
    String email;
    String login;
    String name;
    LocalDate birthday;

    HttpClient client;
    URI url;
    HttpRequest request;
    HttpResponse<String> response;
    Gson gson;
    String json;


    @BeforeEach
    void setUp() {

        userController = new UserController();

        email = "some@email.com";
        login = "someLogin";
        name = "someFilmName";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        birthday = LocalDate.parse("1967-03-25", formatter);

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/users");

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

    }

    @Test
    void addUser() {

        // Добавили фильм стандартным способом без ошибок
        user = new User(email, login, birthday);
        user.setName(name);

        userController.addUser(user);

        assertEquals(1, userController.getAllUsers().size());
        assertEquals(1, userController.getAllUsers().get(0).getId());
        assertEquals(name, userController.getAllUsers().get(0).getName());
        assertEquals(login, userController.getAllUsers().get(0).getLogin());
        assertEquals(email, userController.getAllUsers().get(0).getEmail());
        assertEquals(birthday, userController.getAllUsers().get(0).getBirthday());

    }

    @Test
    void addUserFromHTTPClient() throws IOException, InterruptedException {

        // Добавили фильм c исходными полями
        user = new User(email, login, birthday);
        user.setName(name);

        json = gson.toJson(user);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        user = gson.fromJson(response.body(), User.class);
        assertEquals(email, user.getEmail());

    }

    @Test
    void addFailedFilm() throws IOException, InterruptedException {

        // Добавили фильм c пустым логином
        user = new User(email, "", birthday);
        user.setName(name);

        json = gson.toJson(user);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotEquals(200, response.statusCode());


        // Добавили фильм c логином null
        user = new User(email, null, birthday);
        user.setName(name);

        json = gson.toJson(user);

        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotEquals(200, response.statusCode());

        // Добавили фильм c датой позже текущей
        LocalDate tooLateLocalDate = LocalDate.of(2100, 1, 1);
        user = new User(email, login, tooLateLocalDate);
        user.setName(name);

        json = gson.toJson(user);

        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotEquals(200, response.statusCode());

        // Добавили фильм c неправильной почтой
        user = new User("mailfake@", login, birthday);
        user.setName(name);

        json = gson.toJson(user);

        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotEquals(200, response.statusCode());

    }

    @Test
    void updateFilm() {

        user = new User(email, login, birthday);
        user.setName(name);

        userController.addUser(user);

        assertEquals(1, userController.getAllUsers().size());
        assertEquals(1, userController.getAllUsers().get(0).getId());
        assertEquals(name, userController.getAllUsers().get(0).getName());

        user = new User("another@mail.com", login, birthday);
        user.setName(name);

        user.setId(1);

        userController.updateUser(user);

        assertEquals(1, userController.getAllUsers().size());
        assertEquals(1, userController.getAllUsers().get(0).getId());
        assertEquals("another@mail.com", userController.getAllUsers().get(0).getEmail());

    }

}