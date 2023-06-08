/*
package ru.yandex.practicum.filmorate.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FilmControllerTest {

    FilmController filmController;
    Film film;
    String name;
    String description;
    LocalDate localDate;
    int duration;

    HttpClient client;
    URI url;
    HttpRequest request;
    HttpResponse<String> response;
    Gson gson;
    String json;


    @BeforeEach
    void setUp() {

        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),
                new UserService(new InMemoryUserStorage())));

        name = "someFilmName";
        description = "someDescription";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        localDate = LocalDate.parse("1967-03-25", formatter);
        duration = 120;

        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/films");

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Test
    void addFilm() {

        // Добавили фильм стандартным способом без ошибок
        film = new Film(name, description, localDate, duration);

        filmController.addFilm(film);

        assertEquals(1, filmController.getAllFilms().size());
        assertEquals(1, filmController.getAllFilms().get(0).getFilmId());
        assertEquals(name, filmController.getAllFilms().get(0).getName());
        assertEquals(description, filmController.getAllFilms().get(0).getDescription());
        assertEquals(localDate, filmController.getAllFilms().get(0).getReleaseDate());
        assertEquals(duration, filmController.getAllFilms().get(0).getDuration());

    }

    @Test
    void addFilmFromHTTPClient() throws IOException, InterruptedException {

        // Добавили фильм c исходными полями
        film = new Film(name, description, localDate, duration);

        json = gson.toJson(film);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        film = gson.fromJson(response.body(), Film.class);
        assertEquals(name, film.getName());

    }

    @Test
    void addFailedFilm() throws IOException, InterruptedException {

        // Добавили фильм c пустым именем
        film = new Film("", description, localDate, duration);

        json = gson.toJson(film);

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotEquals(200, response.statusCode());


        // Добавили фильм c именем null
        film = new Film("", description, localDate, duration);

        json = gson.toJson(film);

        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotEquals(200, response.statusCode());

        // Добавили фильм c датой раньше минимальной
        LocalDate tooEarlyLocalDate = LocalDate.of(1800, 1, 1);
        film = new Film(name, description, tooEarlyLocalDate, duration);

        json = gson.toJson(film);

        body = HttpRequest.BodyPublishers.ofString(json);
        request = HttpRequest
                .newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(body)
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotEquals(200, response.statusCode());

        // Добавили фильм c отрицательной длительностью
        film = new Film(name, description, localDate, -100);
        json = gson.toJson(film);

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

        film = new Film(name, description, localDate, duration);

        filmController.addFilm(film);

        assertEquals(1, filmController.getAllFilms().size());
        assertEquals(1, filmController.getAllFilms().get(0).getFilmId());
        assertEquals(name, filmController.getAllFilms().get(0).getName());

        film = new Film("updatedName", description, localDate, duration);
        film.setFilmId(1);

        filmController.updateFilm(film);

        assertEquals(1, filmController.getAllFilms().size());
        assertEquals(1, filmController.getAllFilms().get(0).getFilmId());
        assertEquals("updatedName", filmController.getAllFilms().get(0).getName());

    }

}*/
