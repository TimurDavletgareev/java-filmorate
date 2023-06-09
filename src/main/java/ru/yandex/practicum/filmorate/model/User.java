package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {

    private Integer id;

    @NotBlank
    private String login;
    private String name;
    @NotNull
    @Email
    private String email;
    @Past
    private LocalDate birthday;

    public User(String login, String email, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
    public User(int id, String login, String name, String email, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
    public User(String login, String name, String email, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {
    }

}
