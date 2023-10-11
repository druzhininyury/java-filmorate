package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userStorage.addUser(user);
        log.info("User added: " + user);
        return user;
    }

    public User updateUser(User user) throws ValidationException {
        userStorage.updateUser(user);
        log.info("User updated: " + user);
        return user;
    }

    public User removeUser(int userId) {
        return userStorage.removeUser(userId);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addFriend(int userId, int friendId) {
        return userStorage.addFriend(userId, friendId);
    }

    public User removeFriend(int userId, int friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getAllUserFriends(int userId) {
        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        List<User> user1Friends = getAllUserFriends(user1Id);
        List<User> user2Friends = getAllUserFriends(user2Id);
        user1Friends.retainAll(user2Friends);
        return user1Friends;
    }
}