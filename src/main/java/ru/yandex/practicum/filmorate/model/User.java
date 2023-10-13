package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
public class User {

    private int id;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "[^\\s]+")
    private String login;
    private String name;
    @NotNull
    @Past
    private LocalDate birthday;
    private Map<Integer, FriendStatus> friends = new HashMap<>();

    public User(String email, String login, String name, LocalDate birthdate) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthdate;
    }

    public User(int id, String email, String login, String name, LocalDate birthdate) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthdate;
    }

    public void addFriend(int id) {
        friends.put(id, FriendStatus.REQUEST);
    }

    public void requestFriendship(int id) {
        friends.put(id, FriendStatus.REQUESTED);
    }

    public void confirmFriend(int id) {
        friends.put(id, FriendStatus.CONFIRMED);
    }

    public void removeFriend(int id) {
        friends.remove(id);
    }

    public enum FriendStatus {
        CONFIRMED,
        REQUESTED,
        REQUEST;
    }
}
