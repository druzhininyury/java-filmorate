package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.IncorrectUserIdException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) " +
                          "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);
        user.setId((keyHolder.getKey().intValue()));
        return user;
    }

    @Override
    public User removeUser(int userId) {
        checkIfUserExists(userId);
        User user = getUserById(userId);
        String sqlQueryDeleteUser = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlQueryDeleteUser, userId);
        return user;
    }

    @Override
    public User updateUser(User user) {
        checkIfUserExists(user.getId());
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                    user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users;";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery);
        List<User> users = new ArrayList<>();
        while (userRows.next()) {
            User user = new User(userRows.getInt("id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
            users.add(user);
        }
        return users;
    }

    @Override
    public User getUserById(int userId) {
        checkIfUserExists(userId);
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", userId);
        if (userRows.next()) {
            User user = new User(userRows.getInt("id"),
                                 userRows.getString("email"),
                                 userRows.getString("login"),
                                 userRows.getString("name"),
                                 userRows.getDate("birthday").toLocalDate());
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User addFriend(int userId, int friendId) {
        checkIfUserExists(userId);
        checkIfUserExists(friendId);
        String sqlQuery = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, 'CONFIRMED');";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getUserById(userId);
    }

    @Override
    public User removeFriend(int userId, int friendId) {
        checkIfUserExists(userId);
        checkIfUserExists(friendId);
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getUserById(userId);
    }

    @Override
    public List<User> getAllFriends(int userId) {
        checkIfUserExists(userId);
        List<User> friends = new ArrayList<>();
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?;";
        SqlRowSet friendIdRows = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (friendIdRows.next()) {
            friends.add(getUserById(friendIdRows.getInt("friend_id")));
        }
        return friends;
    }

    private void checkIfUserExists(int userId) {
        String sqlQueryCheckExistence = "SELECT COUNT(id) AS result FROM users WHERE id = ?;";
        SqlRowSet responseRows = jdbcTemplate.queryForRowSet(sqlQueryCheckExistence, userId);
        responseRows.next();
        if (responseRows.getInt("result") == 0) {
            throw new IncorrectUserIdException("User with id " + userId + " doesn't exist.");
        }
    }
}
