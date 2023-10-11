package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         GenreStorage genreStorage,
                         RatingStorage ratingStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.ratingStorage = ratingStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQueryFilms = "INSERT INTO films (name, description, release_date, duration, rate, rating_id) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQueryFilms, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getRate());
            statement.setInt(6, film.getMpa().getId());
            return statement;
        }, keyHolder);
        film.setId((keyHolder.getKey().intValue()));
        if (film.getGenres().size() > 0) {
            StringBuilder sb = new StringBuilder("INSERT INTO films_genres (film_id, genre_id) VALUES");
            for (Genre genre : film.getGenres()) {
                sb.append(" (" + film.getId() + ", " + genre.getId() + "),");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(";");
            jdbcTemplate.update(sb.toString());
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film removeFilm(int filmId) {
        checkIfFilmExists(filmId);
        Film film = getFilmById(filmId);
        String sqlQueryGenres = "DELETE FROM films_genres WHERE film_id = ?;";
        jdbcTemplate.update(sqlQueryGenres, filmId);
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        checkIfFilmExists(film.getId());
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate = ?, rating_id = ? " +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(),
                film.getId());
        String sqlQueryDeleteGenres = "DELETE FROM films_genres WHERE film_id = ?;";
        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());
        if (film.getGenres().size() > 0) {
            StringBuilder sb = new StringBuilder("INSERT INTO films_genres (film_id, genre_id) VALUES");
            for (Genre genre : film.getGenres()) {
                sb.append(" (" + film.getId() + ", " + genre.getId() + "),");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(";");
            jdbcTemplate.update(sb.toString());
        }
        return getFilmById(film.getId());
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
            film.setRate(filmRows.getInt("rate"));
            SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM films_likes WHERE film_id = ?;",
                    film.getId());
            while (likeRows.next()) {
                film.addLike(likeRows.getInt("user_id"));
            }
            SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT genre_id FROM films_genres " +
                            "WHERE film_id = ? ORDER BY genre_id ASC", film.getId());
            while (genreRows.next()) {
                Optional<Genre> genre = genreStorage.getGenreById(genreRows.getInt("genre_id"));
                film.addGenre(genre.get());
            }
            Optional<Rating> rating = ratingStorage.getRatingById(filmRows.getInt("rating_id"));
            if (!rating.isEmpty()) {
                film.setMpa(rating.get());
            }
            films.add(film);
        }
        return films;
    }

    @Override
    public Film getFilmById(int filmId) {
        checkIfFilmExists(filmId);
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", filmId);
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT genre_id FROM films_genres WHERE film_id = ? " +
                "ORDER BY genre_id ASC;", filmId);
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM films_likes WHERE film_id = ?;",
                filmId);
        filmRows.next();
        Film film = new Film();
        film.setId(filmRows.getInt("id"));
        film.setName(filmRows.getString("name"));
        film.setDescription(filmRows.getString("description"));
        film.setReleaseDate(filmRows.getDate("release_date").toLocalDate());
        film.setDuration(filmRows.getInt("duration"));
        film.setRate(filmRows.getInt("rate"));
        while (likeRows.next()) {
            film.addLike(likeRows.getInt("user_id"));
        }
        while (genreRows.next()) {
            Optional<Genre> genre = genreStorage.getGenreById(genreRows.getInt("genre_id"));
            film.addGenre(genre.get());
        }
        Optional<Rating> rating = ratingStorage.getRatingById(filmRows.getInt("rating_id"));
        if (!rating.isEmpty()) {
            film.setMpa(rating.get());
        }
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        checkIfFilmExists(filmId);
        userStorage.checkIfUserExists(userId);
        String sqlQuery = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        checkIfFilmExists(filmId);
        userStorage.checkIfUserExists(userId);
        String sqlQuery = "DELETE FROM films_likes WHERE film_id=? AND user_id=?;";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void checkIfFilmExists(int filmId) {
        String sqlQueryCheckExistence = "SELECT COUNT(id) AS result FROM films WHERE id = ?;";
        SqlRowSet responseRows = jdbcTemplate.queryForRowSet(sqlQueryCheckExistence, filmId);
        responseRows.next();
        if (responseRows.getInt("result") == 0) {
            throw new IncorrectFilmIdException("User with id " + filmId + " doesn't exist.");
        }
    }
}
