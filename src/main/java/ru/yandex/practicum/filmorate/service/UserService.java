package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        return user;
    }

    public User removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user != null && friend != null) {
            user.removeFriend(friendId);
            friend.removeFriend(userId);
            return user;
        } else {
            throw new RuntimeException("No user with id=" + userId);
        }
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getAllUserFriends(int userId) {
        Set<Integer> friendsIds = userStorage.getUserById(userId).getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendsIds) {
            friends.add(userStorage.getUserById(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        List<User> user1Friends = getAllUserFriends(user1Id);
        List<User> user2Friends = getAllUserFriends(user2Id);
        user1Friends.retainAll(user2Friends);
        return user1Friends;
    }
}