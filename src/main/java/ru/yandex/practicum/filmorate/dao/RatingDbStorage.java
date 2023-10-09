package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("ratingDbStorage")
public class RatingDbStorage implements RatingStorage {

    private JdbcTemplate jdbcTemplate;

    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating addRating(Rating rating) {
        String sqlQuery = "INSERT INTO ratings (name) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, rating.getName());
            return statement;
        }, keyHolder);
        rating.setId((keyHolder.getKey().intValue()));
        return rating;
    }

    @Override
    public Optional<Rating> removeRatingById(int ratingId) {
        Optional<Rating> rating = getRatingById(ratingId);
        String sqlQuery = "DELETE FROM ratings WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, ratingId);
        return rating;
    }

    @Override
    public Optional<Rating> getRatingById(int ratingId) {
        String sqlQuery = "SELECT * FROM ratings WHERE id = ?;";
        SqlRowSet ratingRow = jdbcTemplate.queryForRowSet(sqlQuery, ratingId);
        if (ratingRow.next()) {
            Rating rating = new Rating();
            rating.setId(ratingRow.getInt("id"));
            rating.setName(ratingRow.getString("name"));
            return Optional.of(rating);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Rating> getAllRatings() {
        String sqlQuery = "SELECT * FROM ratings;";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Rating> ratings = new ArrayList<>();
        while (ratingRows.next()) {
            Rating rating = new Rating();
            rating.setId(ratingRows.getInt("id"));
            rating.setName(ratingRows.getString("name"));
            ratings.add(rating);
        }
        return ratings;
    }
}
