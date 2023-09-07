package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User removeUser(int userId);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int userId);
}
