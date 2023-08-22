package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private static int nextId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getId() != 0) {
            log.warn("User addition failed: The user has already ID.");
            throw new ValidationException("The user has already ID.");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        log.info("User added: " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (user.getId() <= 0) {
            log.warn("User update failed: the user has no ID.");
            throw new ValidationException("The user has no ID.");
        }
        if (!users.containsKey(user.getId())) {
            log.warn("User update failed: the user has unknown ID.");
            throw new ValidationException("The user has unknown ID.");
        }
        users.put(user.getId(), user);
        log.info("User updated: " + user);
        return user;
    }

    private int getNextId() {
        return nextId++;
    }

}
