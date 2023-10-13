package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private static int nextId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        return users.put(user.getId(), user);
    }

    private static int getNextId() {
        return nextId++;
    }

    @Override
    public User removeUser(int userId) {
        if (!users.containsKey(userId)) {
            log.warn("No user found, the user has unknown ID: " + userId);
            throw new IncorrectRatingIdException("Unknown user id: " + userId);
        }
        return users.remove(userId);
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("User update failed, the user has unknown ID: " + user.getId());
            throw new IncorrectRatingIdException("Unknown user id: " + user.getId());
        }
        return users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int userId) {
        if (!users.containsKey(userId)) {
            log.warn("No user found, the user has unknown ID: " + userId);
            throw new IncorrectRatingIdException("Unknown user id: " + userId);
        }
        return users.get(userId);
    }

    @Override
    public User addFriend(int userId, int friendId) {
        if (users.containsKey(userId) && users.containsKey(friendId)) {
            users.get(userId).addFriend(friendId);
        }
        return users.get(userId);
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        if (users.containsKey(userId) && users.containsKey(friendId)) {
            users.get(userId).removeFriend(friendId);
        }
        return users.get(userId);
    }

    @Override
    public List<User> getAllFriends(int userId) {
        List<User> friends = new ArrayList<>();
        if (users.containsKey(userId)) {
            Set<Integer> friendsIds = users.get(userId).getFriends().keySet();
            for (Integer friendId : friendsIds) {
                friends.add(users.get(friendId));
            }
        }
        return friends;
    }

    @Override
    public void checkIfUserExists(int userId) {
        if (!users.containsKey(userId)) {
            throw new IncorrectUserIdException("No user with id=" + userId);
        }
    }
}
