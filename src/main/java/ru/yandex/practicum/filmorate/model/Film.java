package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;
    private Set<Integer> likes = new HashSet<>();

    @NotNull
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200)
    private final String description;
    @Past
    private final LocalDate releaseDate;
    @Positive
    private final int duration;

    public void addUserToLikedList(Integer userId) {

        likes.add(userId);
    }

    public void removeUserFromLikedList(Integer userId) {
        likes.remove(userId);
    }

    public int getRating() {
        return likes.size();
    }
}
