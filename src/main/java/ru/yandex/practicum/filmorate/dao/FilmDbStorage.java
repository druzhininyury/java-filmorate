package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, RatingStorage ratingStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, genre, rating) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setString(5, film.getGenre().toString());
            statement.setString(6, film.getRating().toString());
            return statement;
        }, keyHolder);
        film.setId((keyHolder.getKey().intValue()));
        return film;
    }

    @Override
    public Film removeFilm(int filmId) {
        Film film = getFilmById(filmId);
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, genre = ?, rating = ? " +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getGenre().toString(), film.getRating().toString(),
                film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("id"));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(filmRows.getDate("release_date").toLocalDate());
            film.setDuration(filmRows.getInt("duration"));
            Optional<Genre> genre = genreStorage.getGenreById(filmRows.getInt("genre"));
            if (!genre.isEmpty()) {
                film.setGenre(genre.get());
            }
            Optional<Rating> rating = ratingStorage.getRatingById(filmRows.getInt("rating"));
            if (!rating.isEmpty()) {
                film.setRating(rating.get());
            }
            films.add(film);
        }
        return films;
    }

    @Override
    public Film getFilmById(int filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", filmId);
        if (filmRows.next()) {
            Film film = new Film();
            film.setId(filmRows.getInt("id"));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(filmRows.getDate("release_date").toLocalDate());
            film.setDuration(filmRows.getInt("duration"));
            Optional<Genre> genre = genreStorage.getGenreById(filmRows.getInt("genre"));
            if (!genre.isEmpty()) {
                film.setGenre(genre.get());
            }
            Optional<Rating> rating = ratingStorage.getRatingById(filmRows.getInt("rating"));
            if (!rating.isEmpty()) {
                film.setRating(rating.get());
            }
            return film;
        } else {
            return null;
        }
    }
}
