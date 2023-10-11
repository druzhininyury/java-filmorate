package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.IncorrectGenreIdException;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("genreDbStorage")
public class GenreDbStorage implements GenreStorage {

    private JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre addGenre(Genre genre) {
        String sqlQuery = "INSERT INTO genres (name) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, genre.getName());
            return statement;
        }, keyHolder);
        genre.setId((keyHolder.getKey().intValue()));
        return genre;
    }

    @Override
    public Optional<Genre> removeGenreById(int genreId) {
        checkIfGenreExists(genreId);
        Optional<Genre> genre = getGenreById(genreId);
        String sqlQuery = "DELETE FROM genres WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, genreId);
        return genre;
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        checkIfGenreExists(genreId);
        String sqlQuery = "SELECT * FROM genres WHERE id = ?;";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlQuery, genreId);
        if (genreRow.next()) {
            Genre genre = new Genre();
            genre.setId(genreRow.getInt("id"));
            genre.setName(genreRow.getString("name"));
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres;";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("id"));
            genre.setName(genreRows.getString("name"));
            genres.add(genre);
        }
        return genres;
    }

    private void checkIfGenreExists(int filmId) {
        String sqlQueryCheckExistence = "SELECT COUNT(id) AS result FROM genres WHERE id = ?;";
        SqlRowSet responseRows = jdbcTemplate.queryForRowSet(sqlQueryCheckExistence, filmId);
        responseRows.next();
        if (responseRows.getInt("result") == 0) {
            throw new IncorrectGenreIdException("Genre with id " + filmId + " doesn't exist.");
        }
    }
}
