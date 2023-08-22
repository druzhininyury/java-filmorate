package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

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

    public User(String email, String login, String name, LocalDate birthdate) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthdate;
    }
}
